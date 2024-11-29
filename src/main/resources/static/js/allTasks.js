function updateTaskStatus(checkbox) {
    const taskId = checkbox.getAttribute("data-task-id");
    const isChecked = checkbox.checked;
    const currentStatus = checkbox.getAttribute("data-task-status");

    const newStatus = currentStatus === "COMPLETED" ? "PENDING" : "COMPLETED";

    fetch(`/tasks/${taskId}/status`, {
        method: "PUT",
        headers: {
            "Content-Type" : "application/json",
        },
        body: JSON.stringify({ status : newStatus}),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            checkbox.setAttribute("data-task-status", newStatus);
            checkbox.checked = (newStatus === "COMPLETED");
        } else {
            alert('상태 수정 실패!');
            checkbox.checked = !isChecked;
        }
    })
    .catch(error => {
            console.error("Error updating task status:", error);
            checkbox.checked = !isChecked;
        });
}

function toggleImportance(star) {
    const taskId = star.getAttribute('data-task-id');
    const currentPriority = star.getAttribute('data-task-priority');

    const isStarred = currentPriority === "STARRED";
    const newPriority = isStarred ? "NONE" : "STARRED";

    if (isStarred) {
        star.classList.remove("fas");
        star.classList.add("far");
    } else {
        star.classList.remove("far");
        star.classList.add("fas");
    }

    fetch(`/tasks/${taskId}/priority`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ "priority": newPriority })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            star.setAttribute('data-task-priority', newPriority);
            updateStar(star, newPriority)
        } else {
            alert('중요도 수정 실패!');
            rollback(star, isStarred);
        }
    })
    .catch(error => {
        console.error('Error updating priority:', error);
        alert('서버와의 통신 오류!');
        rollback(star, isStarred);
    });

    function updateStar(star, priority) {
        const isStarred = (priority === "STARRED");
        star.classList.toggle("active", isStarred);

        if (isStarred) {
            star.classList.remove("far");
            star.classList.add("fas");
        } else {
            star.classList.remove("fas");
            star.classList.add("far");
        }
    }

    function rollback(star, isStarred) {
        star.setAttribute('data-task-priority', isStarred ? "STARRED" : "NONE");
        star.classList.toggle("active");

        if (isStarred) {
            star.classList.remove("fas");
            star.classList.add("far");
        } else {
            star.classList.remove("far");
            star.classList.add("fas");
        }
    }
}