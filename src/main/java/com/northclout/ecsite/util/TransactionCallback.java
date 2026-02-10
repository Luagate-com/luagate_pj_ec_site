package com.northclout.ecsite.util;

import java.sql.Connection;

@FunctionalInterface
public interface TransactionCallback<T> {
  T doInTransaction(Connection conn) throws Exception;
}
