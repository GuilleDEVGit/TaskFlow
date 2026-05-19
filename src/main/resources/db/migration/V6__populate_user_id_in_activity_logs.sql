UPDATE activity_logs al
JOIN users u ON al.username = u.username
SET al.user_id = u.id
WHERE al.user_id IS NULL;