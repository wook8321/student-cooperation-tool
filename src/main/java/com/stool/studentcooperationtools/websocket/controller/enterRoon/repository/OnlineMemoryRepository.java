package com.stool.studentcooperationtools.websocket.controller.enterRoon.repository;

import com.stool.studentcooperationtools.websocket.controller.enterRoon.domain.Online;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineMemoryRepository implements OnlineRepository {

    private final Map<Long, List<Online>> onlineMap = new ConcurrentHashMap<>();

    @Override
    public List<Online> getOnline(final Long roomId) {
        return List.copyOf(onlineMap.getOrDefault(roomId, new ArrayList<>()));
    }

    @Override
    public List<Online> putOnline(final Long roomId, final Online online) {
        List<Online> onlineList = onlineMap.computeIfAbsent(roomId, key -> new ArrayList<>());
        if(!onlineList.contains(online)){
            onlineList.add(online); // 중복없이 안전하게 추가
        }
        return List.copyOf(onlineMap.get(roomId));
    }

    @Override
    public List<Online> removeOnline(final Long roomId, final Online online) {
        List<Online> onlineList = onlineMap.get(roomId);
        if (onlineList != null) {
            onlineList.remove(online);
            if (onlineList.isEmpty()) {
                onlineMap.remove(roomId); // 비어있으면 방 자체 제거
            }
        }
        return List.copyOf(onlineMap.getOrDefault(roomId, new ArrayList<>()));
    }
}
