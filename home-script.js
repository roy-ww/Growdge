// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    initializeAnimations();
    loadNotebooks();
});

// 初始化事件监听器
function initializeEventListeners() {
    // 导航标签切换
    setupNavigationTabs();
    
    // 视图控制
    setupViewControls();
    
    // 筛选控制
    setupFilterControls();
    
    // 笔记本卡片交互
    setupNotebookCards();
    
    // 新建笔记本功能
    setupNewNotebook();
    
    // 模态框控制
    setupModal();
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
    const featuredSection = document.querySelector('.featured-section');
    const recentSection = document.querySelector('.recent-section');
    
    switch(tabType) {
        case 'all':
            featuredSection.style.display = 'block';
            recentSection.style.display = 'block';
            break;
        case 'my':
            featuredSection.style.display = 'none';
            recentSection.style.display = 'block';
            break;
        case 'featured':
            featuredSection.style.display = 'block';
            recentSection.style.display = 'none';
            break;
    }
    
    // 显示切换动画
    animateContentSwitch();
}

// 内容切换动画
function animateContentSwitch() {
    const sections = document.querySelectorAll('section');
    sections.forEach(section => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            section.style.transition = 'all 0.3s ease';
            section.style.opacity = '1';
            section.style.transform = 'translateY(0)';
        }, 50);
    });
}

// 设置视图控制
function setupViewControls() {
    const viewBtns = document.querySelectorAll('.view-btn');
    const notebooksGrid = document.querySelectorAll('.notebooks-grid');
    
    viewBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // 移除所有活动状态
            viewBtns.forEach(b => b.classList.remove('active'));
            
            // 添加当前活动状态
            this.classList.add('active');
            
            // 切换视图
            const viewType = this.dataset.view;
            switchView(viewType, notebooksGrid);
        });
    });
}

// 切换视图
function switchView(viewType, grids) {
    grids.forEach(grid => {
        if (viewType === 'list') {
            grid.style.gridTemplateColumns = '1fr';
            grid.classList.add('list-view');
        } else {
            grid.style.gridTemplateColumns = 'repeat(auto-fill, minmax(280px, 1fr))';
            grid.classList.remove('list-view');
        }
    });
}

// 设置筛选控制
function setupFilterControls() {
    const filterSelect = document.querySelector('.filter-select');
    
    if (filterSelect) {
        filterSelect.addEventListener('change', function() {
            const filterValue = this.value;
            filterNotebooks(filterValue);
        });
    }
}

// 筛选笔记本
function filterNotebooks(filterValue) {
    const featuredCards = document.querySelectorAll('.notebook-card.featured');
    
    // 显示加载状态
    showLoadingState();
    
    // 模拟筛选延迟
    setTimeout(() => {
        featuredCards.forEach(card => {
            // 这里可以根据筛选条件显示/隐藏卡片
            card.style.display = 'block';
        });
        
        hideLoadingState();
        showNotification(`已按 ${getFilterLabel(filterValue)} 筛选`, 'success');
    }, 500);
}

// 获取筛选标签
function getFilterLabel(value) {
    const labels = {
        'recent': '最近',
        'popular': '最受欢迎',
        'newest': '最新'
    };
    return labels[value] || value;
}

// 设置笔记本卡片交互
function setupNotebookCards() {
    const notebookCards = document.querySelectorAll('.notebook-card');
    
    notebookCards.forEach(card => {
        // 卡片点击
        card.addEventListener('click', function(e) {
            // 如果点击的是菜单按钮，不触发卡片点击
            if (e.target.closest('.card-menu')) {
                return;
            }
            
            const isNewNotebook = this.classList.contains('new-notebook');
            
            if (isNewNotebook) {
                openNewNotebookModal();
            } else {
                openNotebook(this);
            }
        });
        
        // 菜单按钮点击
        const menuBtn = card.querySelector('.card-menu');
        if (menuBtn) {
            menuBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                showCardMenu(this, card);
            });
        }
    });
}

// 打开笔记本
function openNotebook(card) {
    const title = card.querySelector('.card-title').textContent;
    console.log('打开笔记本:', title);
    
    // 显示加载状态
    showLoadingState();
    
    // 模拟打开延迟
    setTimeout(() => {
        hideLoadingState();
        showNotification(`正在打开 "${title}"`, 'success');
        
        // 这里可以跳转到笔记本详情页面
        // window.location.href = `notebook.html?id=${card.dataset.id}`;
    }, 1000);
}

// 显示卡片菜单
function showCardMenu(button, card) {
    const title = card.querySelector('.card-title').textContent;
    
    // 创建上下文菜单
    const menu = document.createElement('div');
    menu.className = 'context-menu';
    menu.innerHTML = `
        <div class="menu-item" data-action="open">
            <i class="fas fa-folder-open"></i>
            打开
        </div>
        <div class="menu-item" data-action="rename">
            <i class="fas fa-edit"></i>
            重命名
        </div>
        <div class="menu-item" data-action="duplicate">
            <i class="fas fa-copy"></i>
            复制
        </div>
        <div class="menu-item" data-action="share">
            <i class="fas fa-share"></i>
            分享
        </div>
        <div class="menu-item danger" data-action="delete">
            <i class="fas fa-trash"></i>
            删除
        </div>
    `;
    
    // 添加样式
    menu.style.cssText = `
        position: absolute;
        top: 100%;
        right: 0;
        background-color: #333;
        border: 1px solid #444;
        border-radius: 8px;
        padding: 8px 0;
        min-width: 150px;
        z-index: 1000;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
    `;
    
    // 定位菜单
    const rect = button.getBoundingClientRect();
    menu.style.top = `${rect.bottom + 4}px`;
    menu.style.right = `${window.innerWidth - rect.right}px`;
    
    document.body.appendChild(menu);
    
    // 菜单项点击事件
    menu.addEventListener('click', function(e) {
        const action = e.target.closest('.menu-item')?.dataset.action;
        if (action) {
            handleCardAction(action, card, title);
            document.body.removeChild(menu);
        }
    });
    
    // 点击外部关闭菜单
    document.addEventListener('click', function closeMenu(e) {
        if (!menu.contains(e.target) && e.target !== button) {
            document.body.removeChild(menu);
            document.removeEventListener('click', closeMenu);
        }
    });
}

// 处理卡片操作
function handleCardAction(action, card, title) {
    switch(action) {
        case 'open':
            openNotebook(card);
            break;
        case 'rename':
            renameNotebook(card, title);
            break;
        case 'duplicate':
            duplicateNotebook(card, title);
            break;
        case 'share':
            shareNotebook(card, title);
            break;
        case 'delete':
            deleteNotebook(card, title);
            break;
    }
}

// 重命名笔记本
function renameNotebook(card, currentTitle) {
    const newTitle = prompt('输入新标题:', currentTitle);
    if (newTitle && newTitle !== currentTitle) {
        card.querySelector('.card-title').textContent = newTitle;
        showNotification('笔记本已重命名', 'success');
    }
}

// 复制笔记本
function duplicateNotebook(card, title) {
    showNotification('正在复制笔记本...', 'info');
    
    setTimeout(() => {
        showNotification(`"${title}" 已复制`, 'success');
    }, 1000);
}

// 分享笔记本
function shareNotebook(card, title) {
    if (navigator.share) {
        navigator.share({
            title: title,
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

// 删除笔记本
function deleteNotebook(card, title) {
    if (confirm(`确定要删除 "${title}" 吗？此操作无法撤销。`)) {
        card.style.animation = 'fadeOut 0.3s ease';
        
        setTimeout(() => {
            card.remove();
            showNotification('笔记本已删除', 'success');
        }, 300);
    }
}

// 设置新建笔记本功能
function setupNewNotebook() {
    const newButtons = document.querySelectorAll('.btn-new');
    
    newButtons.forEach(button => {
        button.addEventListener('click', function() {
            openNewNotebookModal();
        });
    });
}

// 打开新建笔记本模态框
function openNewNotebookModal() {
    const modal = document.getElementById('newNotebookModal');
    modal.classList.add('show');
    
    // 聚焦到标题输入框
    const titleInput = document.getElementById('notebookTitle');
    setTimeout(() => titleInput.focus(), 100);
}

// 设置模态框控制
function setupModal() {
    const modal = document.getElementById('newNotebookModal');
    const closeBtn = modal.querySelector('.modal-close');
    const cancelBtn = modal.querySelector('.btn-cancel');
    const form = document.getElementById('newNotebookForm');
    
    // 关闭模态框
    function closeModal() {
        modal.classList.remove('show');
        form.reset();
    }
    
    closeBtn.addEventListener('click', closeModal);
    cancelBtn.addEventListener('click', closeModal);
    
    // 点击背景关闭
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeModal();
        }
    });
    
    // 表单提交
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        createNotebook();
    });
}

// 创建笔记本
function createNotebook() {
    const title = document.getElementById('notebookTitle').value.trim();
    const description = document.getElementById('notebookDescription').value.trim();
    
    if (!title) {
        showNotification('请输入笔记本标题', 'error');
        return;
    }
    
    // 显示创建中状态
    const createBtn = document.querySelector('.btn-create');
    const originalText = createBtn.textContent;
    createBtn.innerHTML = '<span class="loading"></span> 创建中...';
    createBtn.disabled = true;
    
    // 模拟创建延迟
    setTimeout(() => {
        // 创建新笔记本卡片
        addNewNotebookCard(title, description);
        
        // 关闭模态框
        document.getElementById('newNotebookModal').classList.remove('show');
        
        // 重置按钮
        createBtn.textContent = originalText;
        createBtn.disabled = false;
        
        showNotification(`"${title}" 创建成功`, 'success');
    }, 1500);
}

// 添加新笔记本卡片
function addNewNotebookCard(title, description) {
    const recentSection = document.querySelector('.recent-section .notebooks-grid');
    const newCard = document.createElement('div');
    newCard.className = 'notebook-card';
    newCard.innerHTML = `
        <div class="card-icon">
            <i class="fas fa-book" style="color: #FFD700;"></i>
        </div>
        <div class="card-content">
            <h3 class="card-title">${title}</h3>
            <p class="card-meta">${new Date().toLocaleDateString('zh-CN')} · 0个来源</p>
        </div>
        <button class="card-menu" title="更多选项">
            <i class="fas fa-ellipsis-v"></i>
        </button>
    `;
    
    // 添加动画
    newCard.style.opacity = '0';
    newCard.style.transform = 'translateY(20px)';
    
    // 插入到新建笔记本卡片之后
    const newNotebookCard = recentSection.querySelector('.new-notebook');
    recentSection.insertBefore(newCard, newNotebookCard.nextSibling);
    
    // 显示动画
    setTimeout(() => {
        newCard.style.transition = 'all 0.3s ease';
        newCard.style.opacity = '1';
        newCard.style.transform = 'translateY(0)';
    }, 50);
    
    // 重新绑定事件
    setupNotebookCards();
}

// 加载笔记本数据
function loadNotebooks() {
    // 这里可以加载真实的笔记本数据
    console.log('加载笔记本数据...');
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
    
    .context-menu {
        position: absolute;
        background-color: #333;
        border: 1px solid #444;
        border-radius: 8px;
        padding: 8px 0;
        min-width: 150px;
        z-index: 1000;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
    }
    
    .menu-item {
        padding: 8px 16px;
        cursor: pointer;
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 14px;
        color: #ccc;
        transition: background-color 0.2s ease;
    }
    
    .menu-item:hover {
        background-color: #444;
        color: #ffffff;
    }
    
    .menu-item.danger {
        color: #ff6b6b;
    }
    
    .menu-item.danger:hover {
        background-color: #ff6b6b;
        color: #ffffff;
    }
    
    .list-view .notebook-card {
        display: flex;
        align-items: center;
        padding: 16px;
    }
    
    .list-view .card-thumbnail {
        width: 80px;
        height: 60px;
        margin-right: 16px;
        flex-shrink: 0;
    }
    
    .list-view .card-content {
        flex: 1;
        padding: 0;
    }
`;
document.head.appendChild(style);

// 导出函数供外部使用
window.HomePage = {
    showNotification,
    openNotebook,
    createNotebook,
    filterNotebooks
};
