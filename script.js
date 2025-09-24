// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    initializeAnimations();
});

// 初始化事件监听器
function initializeEventListeners() {
    // 来源面板功能
    setupSourcePanel();
    
    // 对话面板功能
    setupDialoguePanel();
    
    // Studio面板功能
    setupStudioPanel();
    
    // 输入框功能
    setupInputField();
}

// 设置来源面板功能
function setupSourcePanel() {
    // 选择所有来源复选框
    const selectAllCheckbox = document.querySelector('.source-controls input[type="checkbox"]');
    const sourceCheckboxes = document.querySelectorAll('.source-item input[type="checkbox"]');
    
    selectAllCheckbox.addEventListener('change', function() {
        sourceCheckboxes.forEach(checkbox => {
            checkbox.checked = this.checked;
        });
    });
    
    // 单个来源复选框
    sourceCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            updateSelectAllState();
        });
    });
    
    // 添加按钮
    const addButton = document.querySelector('.panel-actions .btn-primary');
    addButton.addEventListener('click', function() {
        showAddSourceModal();
    });
    
    // 探索按钮
    const exploreButton = document.querySelector('.panel-actions .btn-secondary');
    exploreButton.addEventListener('click', function() {
        showExploreModal();
    });
}

// 更新"选择所有来源"状态
function updateSelectAllState() {
    const selectAllCheckbox = document.querySelector('.source-controls input[type="checkbox"]');
    const sourceCheckboxes = document.querySelectorAll('.source-item input[type="checkbox"]');
    const checkedCount = Array.from(sourceCheckboxes).filter(cb => cb.checked).length;
    
    selectAllCheckbox.checked = checkedCount === sourceCheckboxes.length;
    selectAllCheckbox.indeterminate = checkedCount > 0 && checkedCount < sourceCheckboxes.length;
}

// 显示添加来源模态框
function showAddSourceModal() {
    // 这里可以添加模态框逻辑
    console.log('显示添加来源模态框');
    alert('添加来源功能 - 这里可以集成文件上传或URL输入功能');
}

// 显示探索模态框
function showExploreModal() {
    // 这里可以添加探索模态框逻辑
    console.log('显示探索模态框');
    alert('探索功能 - 这里可以集成搜索和发现功能');
}

// 设置对话面板功能
function setupDialoguePanel() {
    // 保存到笔记按钮
    const saveButton = document.querySelector('.btn-save');
    saveButton.addEventListener('click', function() {
        saveToNotes();
    });
    
    // 操作按钮
    const actionButtons = document.querySelectorAll('.action-btn');
    actionButtons.forEach(button => {
        button.addEventListener('click', function() {
            const action = this.textContent.trim();
            handleAction(action);
        });
    });
    
    // 建议提示点击
    const promptItems = document.querySelectorAll('.prompt-item');
    promptItems.forEach(item => {
        item.addEventListener('click', function() {
            const prompt = this.textContent.replace('→', '').trim();
            insertPrompt(prompt);
        });
    });
}

// 保存到笔记
function saveToNotes() {
    const content = document.querySelector('.content-text p').textContent;
    console.log('保存到笔记:', content);
    
    // 显示保存成功提示
    showNotification('已保存到笔记', 'success');
}

// 处理操作按钮点击
function handleAction(action) {
    console.log('执行操作:', action);
    
    switch(action) {
        case '添加笔记':
            showAddNoteModal();
            break;
        case '音频概览':
            generateAudioOverview();
            break;
        case '思维导图':
            generateMindMap();
            break;
        default:
            showNotification(`${action}功能开发中...`, 'info');
    }
}

// 显示添加笔记模态框
function showAddNoteModal() {
    alert('添加笔记功能 - 这里可以集成笔记编辑器');
}

// 生成音频概览
function generateAudioOverview() {
    showNotification('正在生成音频概览...', 'info');
    
    // 模拟生成过程
    setTimeout(() => {
        showNotification('音频概览生成完成', 'success');
    }, 3000);
}

// 生成思维导图
function generateMindMap() {
    showNotification('正在生成思维导图...', 'info');
    
    // 模拟生成过程
    setTimeout(() => {
        showNotification('思维导图生成完成', 'success');
    }, 2000);
}

// 插入建议提示到输入框
function insertPrompt(prompt) {
    const inputField = document.querySelector('.input-field');
    inputField.value = prompt;
    inputField.focus();
}

// 设置Studio面板功能
function setupStudioPanel() {
    // Studio卡片点击
    const studioCards = document.querySelectorAll('.studio-card');
    studioCards.forEach(card => {
        card.addEventListener('click', function() {
            const cardType = this.querySelector('span').textContent;
            handleStudioCard(cardType);
        });
    });
    
    // 活动项目点击
    const activityItems = document.querySelectorAll('.activity-item');
    activityItems.forEach(item => {
        item.addEventListener('click', function() {
            const activityName = this.querySelector('.activity-content span').textContent;
            openActivity(activityName);
        });
    });
    
    // 浮动添加按钮
    const floatingAddBtn = document.querySelector('.floating-add-btn');
    floatingAddBtn.addEventListener('click', function() {
        showAddNoteModal();
    });
}

// 处理Studio卡片点击
function handleStudioCard(cardType) {
    console.log('打开Studio卡片:', cardType);
    
    switch(cardType) {
        case '音频概览':
            generateAudioOverview();
            break;
        case '视频概览':
            showNotification('视频概览功能开发中...', 'info');
            break;
        case '思维导图':
            generateMindMap();
            break;
        case '报告':
            generateReport();
            break;
        case '闪卡':
            generateFlashcards();
            break;
        case '测验':
            generateQuiz();
            break;
        default:
            showNotification(`${cardType}功能开发中...`, 'info');
    }
}

// 生成报告
function generateReport() {
    showNotification('正在生成报告...', 'info');
    
    setTimeout(() => {
        showNotification('报告生成完成', 'success');
    }, 4000);
}

// 生成闪卡
function generateFlashcards() {
    showNotification('正在生成智能闪卡...', 'info');
    
    setTimeout(() => {
        showNotification('闪卡生成完成', 'success');
    }, 2500);
}

// 生成测验
function generateQuiz() {
    showNotification('正在生成测验...', 'info');
    
    setTimeout(() => {
        showNotification('测验生成完成', 'success');
    }, 3000);
}

// 打开活动
function openActivity(activityName) {
    console.log('打开活动:', activityName);
    showNotification(`打开 ${activityName}`, 'info');
}

// 设置输入框功能
function setupInputField() {
    const inputField = document.querySelector('.input-field');
    
    // 输入框焦点效果
    inputField.addEventListener('focus', function() {
        this.parentElement.style.borderColor = '#4A90E2';
    });
    
    inputField.addEventListener('blur', function() {
        this.parentElement.style.borderColor = '#444';
    });
    
    // 回车发送
    inputField.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });
}

// 发送消息
function sendMessage() {
    const inputField = document.querySelector('.input-field');
    const message = inputField.value.trim();
    
    if (message) {
        console.log('发送消息:', message);
        
        // 清空输入框
        inputField.value = '';
        
        // 显示发送成功提示
        showNotification('消息已发送', 'success');
        
        // 这里可以添加实际的AI对话逻辑
        simulateAIResponse(message);
    }
}

// 模拟AI响应
function simulateAIResponse(message) {
    setTimeout(() => {
        showNotification('AI正在思考中...', 'info');
        
        setTimeout(() => {
            showNotification('AI响应已生成', 'success');
        }, 2000);
    }, 1000);
}

// 初始化动画
function initializeAnimations() {
    // 为动态添加的元素添加淡入动画
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            mutation.addedNodes.forEach(function(node) {
                if (node.nodeType === 1) { // Element node
                    node.style.opacity = '0';
                    node.style.transform = 'translateY(10px)';
                    
                    setTimeout(() => {
                        node.style.transition = 'all 0.3s ease-out';
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
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// 工具函数：防抖
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// 工具函数：节流
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// 导出函数供外部使用
window.NoteLM = {
    showNotification,
    sendMessage,
    generateAudioOverview,
    generateMindMap,
    generateReport,
    generateFlashcards,
    generateQuiz
};
