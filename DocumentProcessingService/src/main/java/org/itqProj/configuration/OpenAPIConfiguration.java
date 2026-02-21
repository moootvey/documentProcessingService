package org.itqProj.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Document processing API",
                description = "API for document management.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Moootvey",
                        email = "m.slabukhin@gmail.com"
                )
        )
)
public class OpenAPIConfiguration {
}
