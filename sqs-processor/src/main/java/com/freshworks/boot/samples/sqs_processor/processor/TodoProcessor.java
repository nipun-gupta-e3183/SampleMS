package com.freshworks.boot.samples.sqs_processor.processor;

import com.freshworks.boot.common.context.account.AccountContext;
import com.freshworks.boot.common.AccountFetcher;
import com.freshworks.boot.samples.common.model.Account;
import com.freshworks.boot.samples.common.service.TodoService;
import com.freshworks.boot.samples.sqs_processor.dto.TodoDto;
import com.freshworks.boot.samples.sqs_processor.mapper.TodoMapper;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class TodoProcessor {
    private final TodoService todoService;
    private TodoMapper todoMapper;
    private final AccountFetcher<Account> accountFetcher;
    private final AccountContext<Account> accountContext;

    public TodoProcessor(TodoService todoService, TodoMapper todoMapper, AccountFetcher<Account> accountFetcher,
                         AccountContext<Account> accountContext) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
        this.accountFetcher = accountFetcher;
        this.accountContext = accountContext;
    }

    @SqsListener(value = "${sqs.queueName}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void process(TodoDto todoDto) {
        final Account account = accountFetcher.fetchAccount(todoDto.getProduct(), todoDto.getAccountId());
        accountContext.with(account, () -> todoService.addTodo(todoMapper.convert(todoDto)));
    }

}
