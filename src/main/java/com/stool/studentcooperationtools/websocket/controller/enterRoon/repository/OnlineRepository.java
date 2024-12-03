package com.stool.studentcooperationtools.websocket.controller.enterRoon.repository;

import com.stool.studentcooperationtools.websocket.controller.enterRoon.domain.Online;

import java.util.List;

public interface OnlineRepository {

    public List<Online> getOnline(final Long roomId);

    public List<Online> putOnline(final Long roomId, final Online online);

    public List<Online> removeOnline(final Long roomId, final Online online);

}
