package taskflow.repository;

import org.springframework.data.jpa.domain.Specification;
import taskflow.entity.Task;
import taskflow.entity.TaskStatus;

public class TaskSpecification {

    public static Specification<Task> hasUserId(Integer userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Task> titleContains(String title) {
        return (root, query, cb) ->
                title == null ? null :
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
}