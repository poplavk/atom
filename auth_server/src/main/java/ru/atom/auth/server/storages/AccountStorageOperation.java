package ru.atom.auth.server.storages;


import ru.atom.auth.server.base.User;

public interface AccountStorageOperation {

    public boolean addAccount(String userName, String password);

    boolean isUserExist(String userName);

    User getUser(String name);

}
