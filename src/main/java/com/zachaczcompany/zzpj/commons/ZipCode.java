package com.zachaczcompany.zzpj.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Getter
@Setter(PACKAGE)
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class ZipCode {
    @Pattern(regexp = "\\d{2}-\\d{3}")
    private String zipCode;
}
