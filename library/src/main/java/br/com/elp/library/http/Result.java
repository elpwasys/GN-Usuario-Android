package br.com.elp.library.http;

import java.util.Set;

/**
 * Created by pascke on 18/08/16.
 */
public class Result {

    public Status status;
    public Set<String> messages;

    public enum Status {
        FAILURE,
        SUCCESS
    }


}
