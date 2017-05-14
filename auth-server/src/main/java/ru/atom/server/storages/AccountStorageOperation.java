package ru.atom.server.storages;


import ru.atom.server.base.User;

public interface AccountStorageOperation {

    public boolean addAccount(String userName, String password);

    boolean isUserExist(String userName);

    User getUser(String name);

}
