// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    initializeAnimations();
    loadNotes();
});

// 初始化事件监听器
function initializeEventListeners() {
    // 导航标签切换
    setupNavigationTabs();
    
    // 视图控制
    setupViewControls();
    
    // 筛选控制
    setupFilterControls();
    
    // 笔记列表项交互
    setupNoteItems();
    
    // 上下文菜单
    setupContextMenu();
    
    // 新建按钮
    setupNewButton();
}

// 设置导航标签
function setupNavigationTabs() {
    const navTabs = document.querySelectorAll('.nav-tab');
    
    navTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            // 移除所有活动状态
            navTabs.forEach(t => t.classList.remove('active'));
            
            // 添加当前活动状态
            this.classList.add('active');
            
            // 根据标签切换内容
            const tabType = this.dataset.tab;
            switchContent(tabType);
        });
    });
}

// 切换内容显示
function switchContent(tabType) {
    console.log('切换到标签:', tabType);
    
    // 这里可以根据标签类型加载不同的内容
    switch(tabType) {
        case 'all':
            loadAllNotes();
            break;
        case 'my':
            loadMyNotes();
            break;
        case 'featured':
            loadFeaturedNotes();
            break;
    }
    
    showNotification(`已切换到${getTabLabel(tabType)}`, 'success');
}

// 获取标签标签
function getTabLabel(tabType) {
    const labels = {
        'all': '全部',
        'my': '我的笔记本',
        'featured': '精选笔记本'
    };
    return labels[tabType] || tabType;
}

// 设置视图控制
function setupViewControls() {
    const viewBtns = document.querySelectorAll('.view-btn');
    
    viewBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // 移除所有活动状态
            viewBtns.forEach(b => b.classList.remove('active'));
            
            // 添加当前活动状态
            this.classList.add('active');
            
            // 切换视图
            const viewType = this.dataset.view;
            switchView(viewType);
        });
    });
}

// 切换视图
function switchView(viewType) {
    const notesList = document.querySelector('.notes-list');
    
    if (viewType === 'grid') {
        // 切换到网格视图
        notesList.classList.add('grid-view');
        showNotification('已切换到网格视图', 'info');
    } else {
        // 切换到列表视图
        notesList.classList.remove('grid-view');
        showNotification('已切换到列表视图', 'info');
    }
}

// 设置筛选控制
function setupFilterControls() {
    const filterSelects = document.querySelectorAll('.filter-select');
    
    filterSelects.forEach(select => {
        select.addEventListener('change', function() {
            const filterValue = this.value;
            filterNotes(filterValue);
        });
    });
}

// 筛选笔记
function filterNotes(filterValue) {
    const listItems = document.querySelectorAll('.list-item');
    
    // 显示加载状态
    showLoadingState();
    
    // 模拟筛选延迟
    setTimeout(() => {
        listItems.forEach(item => {
            // 这里可以根据筛选条件显示/隐藏项目
            item.style.display = 'grid';
        });
        
        hideLoadingState();
        showNotification(`已按 ${getFilterLabel(filterValue)} 筛选`, 'success');
    }, 500);
}

// 获取筛选标签
function getFilterLabel(value) {
    const labels = {
        'recent': '最近',
        'oldest': '最旧',
        'name': '按名称',
        'sources': '按来源数'
    };
    return labels[value] || value;
}

// 设置笔记项目交互
function setupNoteItems() {
    const noteItems = document.querySelectorAll('.list-item');
    
    noteItems.forEach(item => {
        // 项目点击
        item.addEventListener('click', function(e) {
            // 如果点击的是操作按钮，不触发项目点击
            if (e.target.closest('.action-btn')) {
                return;
            }
            
            openNote(this);
        });
        
        // 操作按钮点击
        const actionBtns = item.querySelectorAll('.action-btn');
        actionBtns.forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.stopPropagation();
                
                const action = this.title;
                handleNoteAction(action, item);
            });
        });
        
        // 菜单按钮点击
        const menuBtn = item.querySelector('.menu-btn');
        if (menuBtn) {
            menuBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                showContextMenu(this, item);
            });
        }
    });
}

// 打开笔记
function openNote(item) {
    const noteId = item.dataset.id;
    const noteName = item.querySelector('.name').textContent;
    
    console.log('打开笔记:', noteName, 'ID:', noteId);
    
    // 显示加载状态
    showLoadingState();
    
    // 模拟打开延迟
    setTimeout(() => {
        hideLoadingState();
        showNotification(`正在打开 "${noteName}"`, 'success');
        
        // 这里可以跳转到笔记详情页面
        // window.location.href = `notebook.html?id=${noteId}`;
    }, 1000);
}

// 处理笔记操作
function handleNoteAction(action, item) {
    const noteName = item.querySelector('.name').textContent;
    
    switch(action) {
        case '打开':
            openNote(item);
            break;
        case '分享':
            shareNote(item, noteName);
            break;
        case '更多选项':
            showContextMenu(item.querySelector('.menu-btn'), item);
            break;
    }
}

// 分享笔记
function shareNote(item, noteName) {
    if (navigator.share) {
        navigator.share({
            title: noteName,
            text: '查看这个NotebookLM笔记本',
            url: window.location.href
        });
    } else {
        // 复制链接到剪贴板
        navigator.clipboard.writeText(window.location.href).then(() => {
            showNotification('链接已复制到剪贴板', 'success');
        });
    }
}

// 设置上下文菜单
function setupContextMenu() {
    const contextMenu = document.getElementById('contextMenu');
    
    // 点击外部关闭菜单
    document.addEventListener('click', function(e) {
        if (!contextMenu.contains(e.target)) {
            contextMenu.classList.remove('show');
        }
    });
    
    // 菜单项点击
    contextMenu.addEventListener('click', function(e) {
        const menuItem = e.target.closest('.menu-item');
        if (menuItem) {
            const action = menuItem.dataset.action;
            const targetItem = contextMenu.dataset.targetItem;
            
            if (targetItem) {
                const item = document.querySelector(`[data-id="${targetItem}"]`);
                if (item) {
                    handleContextMenuAction(action, item);
                }
            }
            
            contextMenu.classList.remove('show');
        }
    });
}

// 显示上下文菜单
function showContextMenu(button, item) {
    const contextMenu = document.getElementById('contextMenu');
    const rect = button.getBoundingClientRect();
    
    // 设置菜单位置
    contextMenu.style.left = `${rect.right - 150}px`;
    contextMenu.style.top = `${rect.bottom + 4}px`;
    
    // 设置目标项目
    contextMenu.dataset.targetItem = item.dataset.id;
    
    // 显示菜单
    contextMenu.classList.add('show');
}

// 处理上下文菜单操作
function handleContextMenuAction(action, item) {
    const noteName = item.querySelector('.name').textContent;
    
    switch(action) {
        case 'open':
            openNote(item);
            break;
        case 'edit':
            editNote(item, noteName);
            break;
        case 'duplicate':
            duplicateNote(item, noteName);
            break;
        case 'share':
            shareNote(item, noteName);
            break;
        case 'favorite':
            favoriteNote(item, noteName);
            break;
        case 'delete':
            deleteNote(item, noteName);
            break;
    }
}

// 编辑笔记
function editNote(item, noteName) {
    const newName = prompt('输入新名称:', noteName);
    if (newName && newName !== noteName) {
        item.querySelector('.name').textContent = newName;
        showNotification('笔记已重命名', 'success');
    }
}

// 复制笔记
function duplicateNote(item, noteName) {
    showNotification('正在复制笔记...', 'info');
    
    setTimeout(() => {
        showNotification(`"${noteName}" 已复制`, 'success');
    }, 1000);
}

// 收藏笔记
function favoriteNote(item, noteName) {
    showNotification(`"${noteName}" 已添加到收藏夹`, 'success');
}

// 删除笔记
function deleteNote(item, noteName) {
    if (confirm(`确定要删除 "${noteName}" 吗？此操作无法撤销。`)) {
        item.style.animation = 'fadeOut 0.3s ease';
        
        setTimeout(() => {
            item.remove();
            showNotification('笔记已删除', 'success');
        }, 300);
    }
}

// 设置新建按钮
function setupNewButton() {
    const newButtons = document.querySelectorAll('.btn-new');
    
    newButtons.forEach(button => {
        button.addEventListener('click', function() {
            createNewNote();
        });
    });
}

// 创建新笔记
function createNewNote() {
    const noteName = prompt('输入新笔记名称:');
    if (noteName) {
        addNewNoteItem(noteName);
        showNotification(`"${noteName}" 创建成功`, 'success');
    }
}

// 添加新笔记项目
function addNewNoteItem(noteName) {
    const listItems = document.querySelector('.list-items');
    const newItem = document.createElement('div');
    newItem.className = 'list-item';
    newItem.dataset.id = Date.now();
    
    newItem.innerHTML = `
        <div class="item-icon">
            <div class="icon-circle gray">
                <span class="icon-text">N</span>
            </div>
        </div>
        <div class="item-name">
            <span class="name">${noteName}</span>
        </div>
        <div class="item-source">
            <span class="source-count">0个来源</span>
        </div>
        <div class="item-date">
            <span class="date">${new Date().toLocaleDateString('zh-CN')}</span>
        </div>
        <div class="item-role">
            <span class="role">Reader</span>
        </div>
        <div class="item-actions">
            <button class="action-btn" title="打开">
                <i class="fas fa-folder-open"></i>
            </button>
            <button class="action-btn" title="分享">
                <i class="fas fa-share"></i>
            </button>
            <button class="action-btn menu-btn" title="更多选项">
                <i class="fas fa-ellipsis-v"></i>
            </button>
        </div>
    `;
    
    // 添加动画
    newItem.style.opacity = '0';
    newItem.style.transform = 'translateY(20px)';
    
    listItems.appendChild(newItem);
    
    // 显示动画
    setTimeout(() => {
        newItem.style.transition = 'all 0.3s ease';
        newItem.style.opacity = '1';
        newItem.style.transform = 'translateY(0)';
    }, 50);
    
    // 重新绑定事件
    setupNoteItems();
}

// 加载笔记数据
function loadNotes() {
    console.log('加载笔记数据...');
}

// 加载全部笔记
function loadAllNotes() {
    console.log('加载全部笔记');
}

// 加载我的笔记
function loadMyNotes() {
    console.log('加载我的笔记');
}

// 加载精选笔记
function loadFeaturedNotes() {
    console.log('加载精选笔记');
}

// 初始化动画
function initializeAnimations() {
    // 为动态添加的元素添加淡入动画
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            mutation.addedNodes.forEach(function(node) {
                if (node.nodeType === 1) { // Element node
                    node.style.opacity = '0';
                    node.style.transform = 'translateY(20px)';
                    
                    setTimeout(() => {
                        node.style.transition = 'all 0.3s ease';
                        node.style.opacity = '1';
                        node.style.transform = 'translateY(0)';
                    }, 10);
                }
            });
        });
    });
    
    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
}

// 显示加载状态
function showLoadingState() {
    const loadingOverlay = document.createElement('div');
    loadingOverlay.className = 'loading-overlay';
    loadingOverlay.innerHTML = '<div class="loading"></div>';
    loadingOverlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 2000;
    `;
    
    document.body.appendChild(loadingOverlay);
}

// 隐藏加载状态
function hideLoadingState() {
    const loadingOverlay = document.querySelector('.loading-overlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
}

// 显示通知
function showNotification(message, type = 'info') {
    // 创建通知元素
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    
    // 添加样式
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background-color: ${type === 'success' ? '#4CAF50' : type === 'error' ? '#F44336' : '#2196F3'};
        color: white;
        padding: 12px 20px;
        border-radius: 6px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        z-index: 1000;
        font-size: 14px;
        opacity: 0;
        transform: translateX(100%);
        transition: all 0.3s ease-out;
    `;
    
    document.body.appendChild(notification);
    
    // 显示动画
    setTimeout(() => {
        notification.style.opacity = '1';
        notification.style.transform = 'translateX(0)';
    }, 10);
    
    // 自动隐藏
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(100%)';
        
        setTimeout(() => {
            if (document.body.contains(notification)) {
                document.body.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// 添加CSS动画
const style = document.createElement('style');
style.textContent = `
    @keyframes fadeOut {
        from {
            opacity: 1;
            transform: scale(1);
        }
        to {
            opacity: 0;
            transform: scale(0.8);
        }
    }
    
    .grid-view .list-header {
        display: none;
    }
    
    .grid-view .list-items {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 20px;
        padding: 20px;
    }
    
    .grid-view .list-item {
        display: flex;
        flex-direction: column;
        padding: 20px;
        border: 1px solid #333;
        border-radius: 12px;
        background-color: #2a2a2a;
    }
    
    .grid-view .item-icon {
        margin-bottom: 16px;
    }
    
    .grid-view .item-name {
        margin-bottom: 12px;
    }
    
    .grid-view .item-source,
    .grid-view .item-date,
    .grid-view .item-role {
        margin-bottom: 8px;
    }
    
    .grid-view .item-actions {
        opacity: 1;
        margin-top: auto;
        justify-content: center;
    }
`;
document.head.appendChild(style);

// 导出函数供外部使用
window.NotesList = {
    showNotification,
    openNote,
    createNewNote,
    filterNotes
};
