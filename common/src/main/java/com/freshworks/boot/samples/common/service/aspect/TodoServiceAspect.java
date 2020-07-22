package com.freshworks.boot.samples.common.service.aspect;

import com.freshworks.boot.common.context.account.AccountContext;
import com.freshworks.boot.samples.common.model.Account;
import com.freshworks.boot.samples.common.service.TodoService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TodoServiceAspect {


    @Before("execution(* com.freshworks.boot.samples.common.service.TodoService.*(..)) && target(todoServiceService)")
    public void aroundExecution(JoinPoint pjp, TodoService todoServiceService) throws Throwable {
        Filter filter = todoServiceService.entityManager.unwrap(Session.class).enableFilter("accountFilter");
        filter.setParameter("accountId", todoServiceService.getAccountID());
        filter.validate();
    }
}
