package aspects;

import annotation.Perfis;
import domain.ProfileAggregate.Enumerations.ProfilesEnum;
import helpers.SingletonPerfis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
public class AuthorizationAspect {

    @Around("execution(* appServices.*.*(..))")
    public Object DoAuthorizationAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (method.isAnnotationPresent(Perfis.class) == false) {
            return joinPoint.proceed();
        }

        SingletonPerfis loggedProfiles = SingletonPerfis.getSingletonPerfis();
        Perfis annotatedProfiles = method.getAnnotation(Perfis.class);



        List<String> loggedProfilesList = Arrays.asList(loggedProfiles.getPerfis());

        List<String> annotatedProfilesList = new ArrayList<>();
        for (ProfilesEnum profile : annotatedProfiles.nomes()) {
            annotatedProfilesList.add(profile.toString());
        }

        Set<String> intersection = annotatedProfilesList.stream()
                .distinct()
                .filter(loggedProfilesList::contains)
                .collect(Collectors.toSet());

        if (intersection.stream().count() < 1) {
            throw new RuntimeException("Perfil não autorizado para a execução do método " + method.getName());
        }

        return joinPoint.proceed();
    }
}
