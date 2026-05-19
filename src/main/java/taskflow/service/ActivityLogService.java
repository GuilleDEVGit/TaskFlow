package taskflow.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import taskflow.dto.ActivityLogResponse;
import taskflow.entity.ActionType;
import taskflow.entity.ActivityLog;
import taskflow.repository.ActivityLogRepository;
import taskflow.repository.ActivityLogSpecification;


@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    //GET
    public Page<ActivityLogResponse> findAll(Long userId, String username, ActionType actionType, String title, Pageable pageable) {

        Specification<ActivityLog> spec = Specification.allOf(
                ActivityLogSpecification.hasUserId(userId),
                ActivityLogSpecification.hasUsername(username),
                ActivityLogSpecification.hasActionType(actionType),
                ActivityLogSpecification.titleContains(title)
        );

        Page<ActivityLog> activityLogsPage = activityLogRepository.findAll(spec, pageable);

       return activityLogsPage.map(activityLog -> new ActivityLogResponse(
                activityLog.getId(),
                activityLog.getUserId(),
                activityLog.getUsername(),
                activityLog.getAction(),
                activityLog.getDetails(),
                activityLog.getCreatedAt()
        ));
    }
}
