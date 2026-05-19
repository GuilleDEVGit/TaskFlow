package taskflow.repository;

import org.springframework.data.jpa.domain.Specification;
import taskflow.entity.ActionType;
import taskflow.entity.ActivityLog;

public class ActivityLogSpecification {

    public static Specification<ActivityLog> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("userId"), userId);
        };
    }

    public static Specification<ActivityLog> hasUsername(String username) {
        return (root, query, cb) ->
                username == null ? null : cb.equal(root.get("username"), username);
    }

    public static Specification<ActivityLog> hasActionType(ActionType actionType) {
        return (root, query, cb) ->
                actionType == null ? null : cb.equal(root.get("action"), actionType);
    }

    public static Specification<ActivityLog> titleContains(String title) {
        return (root, query, cb) ->
                title == null ? null :
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
}
