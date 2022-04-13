package io.github.aj8gh.countdown.slack;

public class Main {

    public static void main(String[] args) {
        var client = new SlackClient();
        client.postMessage("#bot-test", "Hello, World!");
    }
}
