package com.lamarfishing.core.schedule.resolver;

import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleResolver {

    private final ScheduleRepository scheduleRepository;

    public Long resolve(String publicId) {
        return scheduleRepository.findIdByPublicId(publicId)
                .orElseThrow(ScheduleNotFound::new);
    }
}
