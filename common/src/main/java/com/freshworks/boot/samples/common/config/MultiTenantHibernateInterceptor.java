package com.freshworks.boot.samples.common.config;

import com.freshworks.boot.common.context.account.AccountContext;
import com.freshworks.boot.samples.common.model.Account;
import com.freshworks.boot.samples.common.model.TenantSupport;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class MultiTenantHibernateInterceptor extends EmptyInterceptor {

    @Autowired
    private AccountContext<Account> accountContext;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return updateTenantInfo(entity, "save", state, propertyNames, true);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        updateTenantInfo(entity, "delete", state, propertyNames, false);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return updateTenantInfo(entity, "flush-dirty", currentState, propertyNames, true);

    }

    private boolean updateTenantInfo(Object entity, String action, Object[] currentState, String[] propertyNames, boolean updateState) {
        Class<?> clazz = entity.getClass().getSuperclass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(TenantSupport.class)) {
                try {
                    Annotation annotation = field.getAnnotation(TenantSupport.class);
                    String tenantField = ((TenantSupport) annotation).tenantField();
                    Object account = accountContext.get();
                    Method tenantGetter = new PropertyDescriptor(tenantField, account.getClass()).getReadMethod();
                    String tenantInfo = (String) ReflectionUtils.invokeMethod(tenantGetter, account);
                    if (updateState) {
                        log.debug("[" + action + "] Updating the entity state with tenant information: " + tenantInfo);
                        for (int i = 0; i < propertyNames.length; i++) {
                            if (field.getName().equals(propertyNames[i])) {
                                currentState[i] = tenantInfo;
                                break;
                            }
                        }
                    } else {
                        log.debug("[" + action + "] Updating the entity with tenant information: " + tenantInfo);
                        field.set(entity, tenantInfo);
                    }
                    return true;
                } catch (Exception e) {
                    log.error("Error while updating the entity field.", e);
                }
            }
        }
        return false;
    }
}
