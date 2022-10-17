package com.netease.nim.camellia.redis.proxy.samples.auth;

import com.netease.nim.camellia.redis.proxy.auth.ClientAuthProvider;
import com.netease.nim.camellia.redis.proxy.auth.ClientIdentity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class MockClientAuthProvider implements ClientAuthProvider {

    // Mock sample client data
    // TODO: Implement custom auth provider, read clients from database or other places
    private final Map<String, Client> clients = Stream.of(
            new Client("client1", "pass", 1L, "default"),
            new Client("client2", "6377", 1L, "group6377"),
            new Client("client3", "6379", 1L, "group6379")
    ).collect(java.util.stream.Collectors.toMap(Client::getPassword, client -> client));

    @Override
    public ClientIdentity auth(String userName, String password) {
        ClientIdentity clientIdentity = new ClientIdentity();
        Client client = clients.get(password);
        if (client == null) {
            clientIdentity.setPass(false);
        } else {
            clientIdentity.setBid(client.getBid());
            clientIdentity.setBgroup(client.getBgroup());
            clientIdentity.setPass(true);
        }
        return clientIdentity;
    }

    @Override
    public boolean isPasswordRequired() {
        return true;
    }


    /**
     * // TODO:
     * create table clients
     * (
     * id       bigint primary key auto_increment,
     * name     varchar(50) not null,
     * password varchar(50) not null,
     * bid      int         not null,
     * bgroup   varchar(50) not null
     * )
     */
    static class Client {
        private String name;
        private String password;
        private long bid;
        private String bgroup;

        public Client(String name, String password, long bid, String bgroup) {
            this.name = name;
            this.password = password;
            this.bid = bid;
            this.bgroup = bgroup;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public long getBid() {
            return bid;
        }

        public String getBgroup() {
            return bgroup;
        }
    }

}
