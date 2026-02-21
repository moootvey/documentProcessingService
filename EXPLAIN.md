EXPLAIN для запроса поиска документов

Ниже - пример SQL-запроса, который выполняет поиск документов с определёнными условиями:

```sql
EXPLAIN (ANALYZE) SELECT id, author, created_at, status, title, unique_number, updated_at
                  FROM document_metadata d
                  WHERE status = 'APPROVED'
                    AND author = 'John Doe'
                    AND created_at >= '2026-02-20 09:38:37+00'
                    AND created_at <= '2026-02-22 09:38:37+00'
                  ORDER BY author ASC
                      LIMIT 20 OFFSET 0;
```
Результат выполнения EXPLAIN (ANALYZE) выглядит следующим образом:

```
Limit  (cost=0.29..8.31 rows=1 width=127) (actual time=0.041..0.044 rows=2.00 loops=1)
  Buffers: shared hit=3
  ->  Index Scan using idx_docs_search_status_author_date on document_metadata  (cost=0.29..8.31 rows=1 width=127) (actual time=0.040..0.042 rows=2.00 loops=1)
        Index Cond: (((status)::text = 'APPROVED'::text) AND ((author)::text = 'John Doe'::text) AND (created_at >= '2026-02-20 09:38:37+00'::timestamp with time zone) AND (created_at <= '2026-02-22 09:38:37+00'::timestamp with time zone))
        Index Searches: 1
        Buffers: shared hit=3
Planning Time: 0.191 ms
Execution Time: 0.073 ms
```

Рассматривая полученный результат использования индекса через команду `EXPLAIN (ANALYZE)`, можно сделать вывод, что планировщик PostgreSQL выбрал оптимальный способ выполнения запроса, используя Index Scan по индексу `idx_docs_search_status_author_date`. Этот индекс полностью покрывает условия WHERE, что позволяет эффективно сузить выборку и применить сортировку с лимитом без необходимости сканировать всю таблицу.

Индекс, который использовался, выглядит следующим образом:

```sql
CREATE INDEX IF NOT EXISTS idx_docs_search_status_author_date
  ON document_metadata (status, author, created_at);
```

Он представляет собой B-tree индекс с колонками `status`, `author` и `created_at`. Он позволяет ускорить использование операции `WHERE` с точными сравнения по первым колонкам, а затем диапазон по полю `created_at`. Это особенно эффективно при наличии большого количества данных, так как позволяет быстро сузить выборку до нужных строк.

Его эффективности зависит от селективности и совпадения фильтров запроса с префиксом индекса. В данном случае, так как запрос использует точные сравнения по `status` и `author`, а также диапазон по `created_at`, индекс является оптимальным выбором для данного набора условий.

Однако, если бы в запросе использовались функции (например, `lower(author)`) или операторы `ILIKE`, то обычный индекс по `author` не был бы использован, и потребовался бы функциональный индекс. Также важно следить за типами данных и статистикой, чтобы планировщик мог правильно оценить стоимость выполнения запроса и выбрать оптимальный план действий.