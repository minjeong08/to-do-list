function toggleImportance(star) {
    const taskId = star.getAttribute('data-task-id');
    let priority = star.getAttribute('data-task-priority');

    const isStarred = priority === "STARRED";
    priority = isStarred ? "NONE" : "STARRED";
    star.setAttribute('data-task-priority', priority);
    star.classList.toggle("active");

    if (isStarred) {
        star.classList.remove("fas");
        star.classList.add("far");
    } else {
        star.classList.remove("far");
        star.classList.add("fas");
    }

    fetch(`/tasks/${taskId}/update/priority`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ "priority": priority })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            location.reload();
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