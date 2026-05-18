package taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.ActivityLogResponse;
import taskflow.entity.ActionType;
import taskflow.service.ActivityLogService;

@RestController
@RequestMapping("/api/logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @Operation(
            summary = "Get all records of activity logs"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public Page<ActivityLogResponse> getActivityLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false)ActionType actionType,
            @RequestParam(required = false) String title,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return activityLogService.findAll(username,actionType,title,pageable);
    }

}
