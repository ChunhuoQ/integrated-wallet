<template>
  <el-dropdown trigger="click" @command="handleCommand">
    <span class="github-app-actions">
      <svg-icon icon-class="github" />
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="install">Install GitHub App</el-dropdown-item>
      <el-dropdown-item command="uninstall">Uninstall GitHub App</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
export default {
  name: 'RuoYiGit',
  data() {
    return {
      installUrl: process.env.VUE_APP_GITHUB_APP_INSTALL_URL
        || 'https://github.com/apps',
      uninstallUrl: process.env.VUE_APP_GITHUB_APP_UNINSTALL_URL
        || 'https://github.com/settings/installations'
    }
  },
  methods: {
    handleCommand(command) {
      const urlMap = {
        install: this.installUrl,
        uninstall: this.uninstallUrl
      }
      window.open(urlMap[command], '_blank')
    }
  }
}
</script>

<style scoped>
.github-app-actions {
  display: inline-flex;
  align-items: center;
  height: 100%;
  cursor: pointer;
}
</style>
