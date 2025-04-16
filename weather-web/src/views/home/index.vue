<template>
  <div class="warning-item">
    <div class="warning-content">
      <div class="warning-title">
        <span class="warning-type">{{ warning.type }}{{ warning.level }}预警</span>
        <span class="warning-time">发布于{{ warning.updateTime }}</span>
      </div>
      <div class="warning-detail">{{ warning.content }}</div>
    </div>
  </div>
</template>

<style scoped>
.warning-item {
  margin-bottom: 15px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 15px;
}

.warning-content {
  display: flex;
  flex-direction: column;
}

.warning-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.warning-type {
  font-size: 16px;
  font-weight: bold;
  color: #ff4d4f;
}

.warning-time {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.warning-detail {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
}
</style> 