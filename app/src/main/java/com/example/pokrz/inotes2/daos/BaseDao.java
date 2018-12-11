package com.example.pokrz.inotes2.daos;

public interface BaseDao<T> {

    void insert(T data);

    void delete(T data);

    void update(T data);

}
