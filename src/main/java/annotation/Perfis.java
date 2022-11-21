package annotation;

import domain.ProfileAggregate.Enumerations.ProfilesEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Perfis {
    ProfilesEnum[] nomes();
}
