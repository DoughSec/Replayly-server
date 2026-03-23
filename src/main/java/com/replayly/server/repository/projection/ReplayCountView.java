package com.replayly.server.repository.projection;

public interface ReplayCountView {
    Long getApiRequestLogId();
    String getRequestName();
    long getReplayCount();
}
