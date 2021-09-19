package com.kaching.libidentity.aspect;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kaching.libidentity.exception.TokenInvalidException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class IdentityVerificationAspect {

    private final HttpServletRequest request;
    private final FirebaseAuth firebaseAuth;

    @Autowired
    public IdentityVerificationAspect(HttpServletRequest request, FirebaseAuth firebaseAuth) {
        this.request = request;
        this.firebaseAuth = firebaseAuth;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    @Pointcut("@annotation(com.kaching.libidentity.annotation.PublicApi)")
    public void publicApiPointcut() {
    }

    @Pointcut("@annotation(com.kaching.libidentity.annotation.Permission)")
    public void permissionPointcut() {
    }

    @Before("controllerPointcut() && !publicApiPointcut()")
    public void verifyToken(JoinPoint joinPoint) throws FirebaseAuthException {
        request.setAttribute("uid", verifyToken());
    }

    @Before("controllerPointcut() && permissionPointcut() && !publicApiPointcut()")
    public void verifyTokenAndPermission(JoinPoint joinPoint) throws FirebaseAuthException {
        String uid = verifyToken();
        verifyPermission(uid);
        request.setAttribute("uid", uid);
    }

    private String verifyToken() throws FirebaseAuthException {
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(getTokenFromRequest());
        return decodedToken.getUid();
    }

    private void verifyPermission(String uid) {
        // verify permission
    }

    private String getTokenFromRequest() {
        String headerValue = request.getHeader("Authorization");
        if(headerValue == null) {
            throw new TokenInvalidException("Invalid authorization");
        } else {
            String[] headerParts = headerValue.split(StringUtils.SPACE);

            if (headerParts.length < 2) {
                throw new TokenInvalidException("Token is invalid");
            } else {
                return headerParts[1];
            }
        }
    }
}
