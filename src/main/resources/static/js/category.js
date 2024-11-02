// 카테고리 추가
function addCategory() {
    const cateName = document.getElementById('newCateName').value;

    fetch('/category/new', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cateName })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            location.reload();
        } else {
            alert('카테고리 추가에 실패했습니다.');
        }
    });
}

// 카테고리 수정 모달 열기
function openEditModal(button) {
    const id = button.getAttribute('data-id');
    const cateName = button.getAttribute('data-name');

    document.getElementById('editCategoryId').value = id;
    document.getElementById('editCateName').value = cateName;
    const editModal = new bootstrap.Modal(document.getElementById('editCategoryModal'));
    editModal.show();
}

// 카테고리 수정
function updateCategory() {
        const id = document.getElementById('editCategoryId').value;
        const cateName = document.getElementById('editCateName').value;

        fetch(`/category/update/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ cateName: cateName })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            } else {
                alert('카테고리 수정에 실패했습니다.');
            }
        });
}

// 카테고리 삭제
function deleteCategory(button) {
    const id = button.getAttribute('data-id');

    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    fetch(`/category/delete/${id}`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            location.reload();
        } else {
            alert('카테고리 삭제에 실패했습니다.');
        }
    });
}