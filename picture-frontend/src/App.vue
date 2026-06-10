<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 font-sans text-gray-700">
    <header class="bg-white/80 backdrop-blur-md shadow-sm sticky top-0 z-20 border-b border-gray-100">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex justify-between items-center">
        <div class="flex items-center space-x-3">
          <div class="w-9 h-9 bg-gradient-to-tr from-blue-500 via-purple-500 to-pink-500 rounded-xl flex items-center justify-center text-white font-bold shadow-md">P</div>
          <h1 class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-purple-600">智能图库</h1>
        </div>
        <nav class="flex items-center space-x-1 bg-gray-100/60 rounded-xl p-1">
          <button v-for="tab in tabs" :key="tab.id" @click="activeTab = tab.id"
            :class="['px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200',
              activeTab === tab.id ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-600 hover:text-gray-900 hover:bg-white/50']">
            <span class="flex items-center space-x-1.5">
              <svg v-if="tab.id === 'pictures'" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              <svg v-if="tab.id === 'albums'" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
              <svg v-if="tab.id === 'tags'" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
              </svg>
              <svg v-if="tab.id === 'stats'" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
              <span>{{ tab.name }}</span>
            </span>
          </button>
        </nav>
        <div class="flex items-center space-x-2">
          <div class="relative">
            <input v-model="globalSearch" type="text" placeholder="搜索图片/专辑/主题词..."
              @keyup.enter="doGlobalSearch"
              class="w-64 pl-10 pr-4 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition" />
            <svg class="w-4 h-4 absolute left-3 top-2.5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
          <button @click="showUploadModal = true"
            class="px-5 py-2 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-lg shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition duration-200 flex items-center space-x-2 text-sm font-medium">
            <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            <span>上传</span>
          </button>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 pb-24">
      <!-- PICTURES -->
      <div v-if="activeTab === 'pictures'">
        <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-4 mb-6">
          <div class="flex flex-wrap items-center gap-3">
            <div class="flex items-center space-x-2">
              <span class="text-sm text-gray-500">筛选:</span>
              <select v-model="filterAlbumId" @change="fetchPictures"
                class="px-3 py-1.5 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="null">全部专辑</option>
                <option v-for="a in allAlbums" :key="a.id" :value="a.id">{{ a.name }} ({{ a.pictureCount }})</option>
              </select>
              <select v-model="filterTagId" @change="fetchPictures"
                class="px-3 py-1.5 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="null">全部主题词</option>
                <option v-for="t in hotTags" :key="t.id" :value="t.id">#{{ t.name }} ({{ t.referenceCount }})</option>
              </select>
            </div>
            <div class="flex-1"></div>
            <div class="flex items-center space-x-3">
              <label class="flex items-center space-x-2 cursor-pointer text-sm text-gray-600 hover:text-gray-900">
                <input type="checkbox" v-model="multiSelectMode" class="w-4 h-4 text-blue-600 rounded focus:ring-blue-500" />
                <span>多选模式</span>
              </label>
              <span v-if="multiSelectMode && selectedPictureIds.length > 0" class="text-sm text-blue-600 font-medium">
                已选 {{ selectedPictureIds.length }} 张
              </span>
            </div>
          </div>
        </div>

        <div v-if="loading" class="flex justify-center py-20">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
        </div>
        <div v-else-if="pictures.length === 0" class="text-center py-20 bg-white rounded-2xl shadow-sm border border-gray-100">
          <div class="text-6xl mb-4">🖼️</div>
          <h3 class="text-xl font-medium text-gray-500">暂无图片，快来上传吧！</h3>
          <button @click="showUploadModal = true"
            class="mt-4 px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition text-sm font-medium">
            立即上传
          </button>
        </div>
        <div v-else class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
          <div v-for="pic in pictures" :key="pic.id"
            class="group relative bg-white rounded-2xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100">
            <div v-if="multiSelectMode" class="absolute top-2 left-2 z-10">
              <input type="checkbox" :value="pic.id" v-model="selectedPictureIds"
                class="w-5 h-5 text-blue-600 rounded bg-white/90 border-2 border-gray-300 focus:ring-blue-500 cursor-pointer" />
            </div>
            <div class="aspect-square bg-gray-100 relative overflow-hidden cursor-pointer" @click="viewPicture(pic)">
              <img :src="pic.url" class="object-cover w-full h-full transform group-hover:scale-105 transition duration-500" loading="lazy" />
              <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-30 transition duration-300"></div>
              <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition duration-300 flex space-x-1">
                <a :href="pic.url" download @click.stop target="_blank"
                  class="p-1.5 bg-white/90 rounded-full text-gray-700 hover:text-blue-600 shadow-md hover:bg-white transition" title="下载">
                  <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
                  </svg>
                </a>
                <button @click.stop="confirmDeletePicture(pic)"
                  class="p-1.5 bg-white/90 rounded-full text-gray-700 hover:text-red-600 shadow-md hover:bg-white transition" title="删除">
                  <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
              <div class="absolute bottom-0 left-0 right-0 p-2 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition duration-300">
                <div class="flex flex-wrap gap-1">
                  <span v-for="tag in (pic.tags || []).slice(0, 3)" :key="tag.id"
                    class="text-[10px] px-1.5 py-0.5 bg-white/30 backdrop-blur-sm rounded text-white">#{{ tag.name }}</span>
                </div>
              </div>
            </div>
            <div class="p-3">
              <h3 class="text-sm font-medium text-gray-800 truncate">{{ pic.name }}</h3>
              <div class="flex justify-between items-center mt-1.5">
                <span class="text-xs text-gray-400">{{ formatTime(pic.createTime) }}</span>
                <span class="text-xs text-gray-400">{{ formatSize(pic.size) }}</span>
              </div>
            </div>
          </div>
        </div>

        <div v-if="multiSelectMode && selectedPictureIds.length > 0"
          class="fixed bottom-6 left-1/2 transform -translate-x-1/2 z-30 bg-white rounded-2xl shadow-2xl border border-gray-100 px-4 py-3 flex items-center space-x-3">
          <span class="text-sm font-medium text-gray-700 border-r border-gray-200 pr-3">已选 {{ selectedPictureIds.length }} 张</span>
          <button @click="showBatchTagModal = true"
            class="px-4 py-2 bg-gradient-to-r from-emerald-500 to-teal-600 text-white rounded-lg text-sm font-medium hover:shadow-lg transition">
            <span class="flex items-center space-x-1">
              <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
              </svg>
              <span>批量加主题词</span>
            </span>
          </button>
          <button @click="showBatchAlbumModal = true"
            class="px-4 py-2 bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-lg text-sm font-medium hover:shadow-lg transition">
            <span class="flex items-center space-x-1">
              <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
              <span>批量归专辑</span>
            </span>
          </button>
          <button @click="confirmBatchDelete"
            class="px-4 py-2 bg-gradient-to-r from-red-500 to-rose-600 text-white rounded-lg text-sm font-medium hover:shadow-lg transition">
            <span class="flex items-center space-x-1">
              <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
              <span>批量删除</span>
            </span>
          </button>
          <button @click="selectedPictureIds = []" class="px-3 py-2 text-gray-500 hover:text-gray-700 text-sm">取消</button>
        </div>
      </div>

      <!-- ALBUMS -->
      <div v-else-if="activeTab === 'albums'">
        <div v-if="!viewingAlbum">
          <div class="flex items-center justify-between mb-6">
            <div>
              <h2 class="text-2xl font-bold text-gray-800">专辑管理</h2>
              <p class="text-sm text-gray-500 mt-1">共 {{ allAlbums.length }} 个专辑</p>
            </div>
            <button @click="openCreateAlbum"
              class="px-5 py-2.5 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-xl shadow-lg hover:shadow-xl transition text-sm font-medium">
              <span class="flex items-center space-x-1.5">
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
                <span>新建专辑</span>
              </span>
            </button>
          </div>
          <div v-if="allAlbums.length === 0" class="text-center py-20 bg-white rounded-2xl shadow-sm">
            <div class="text-6xl mb-4">📁</div>
            <h3 class="text-xl font-medium text-gray-500">还没有专辑</h3>
          </div>
          <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
            <div v-for="album in allAlbums" :key="album.id"
              class="group bg-white rounded-2xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 cursor-pointer"
              @click="openAlbumDetail(album)">
              <div class="aspect-video relative overflow-hidden bg-gradient-to-br from-blue-100 to-purple-100">
                <img v-if="album.coverUrl" :src="album.coverUrl" class="w-full h-full object-cover transform group-hover:scale-105 transition duration-500" />
                <div v-else class="w-full h-full flex items-center justify-center">
                  <svg class="w-16 h-16 text-white/60" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                  </svg>
                </div>
                <div class="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent"></div>
                <div class="absolute top-3 right-3 opacity-0 group-hover:opacity-100 transition duration-300 flex space-x-1">
                  <button @click.stop="openEditAlbum(album)"
                    class="p-2 bg-white/90 rounded-lg text-gray-700 hover:text-blue-600 shadow-md hover:bg-white transition" title="编辑">
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>
                  <button v-if="!album.isDefault" @click.stop="confirmDeleteAlbum(album)"
                    class="p-2 bg-white/90 rounded-lg text-gray-700 hover:text-red-600 shadow-md hover:bg-white transition" title="删除">
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
                <div class="absolute bottom-3 left-4 right-4">
                  <span v-if="album.isDefault" class="inline-block px-2 py-0.5 bg-blue-500/90 text-white text-xs rounded-md mb-2">系统默认</span>
                </div>
              </div>
              <div class="p-4">
                <h3 class="font-semibold text-gray-800 truncate">{{ album.name }}</h3>
                <p v-if="album.description" class="text-xs text-gray-500 mt-1 line-clamp-2">{{ album.description }}</p>
                <div class="flex justify-between items-center mt-3 pt-3 border-t border-gray-100">
                  <span class="text-xs text-gray-500 flex items-center space-x-1">
                    <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    <span>{{ album.pictureCount }} 张</span>
                  </span>
                  <span class="text-xs text-gray-500">{{ formatSize(album.totalSize) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <div class="mb-6">
            <button @click="viewingAlbum = false"
              class="inline-flex items-center space-x-1 text-sm text-gray-600 hover:text-blue-600 transition mb-3">
              <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
              </svg>
              <span>返回专辑列表</span>
            </button>
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-5 flex items-center space-x-5">
              <div class="w-24 h-24 rounded-xl overflow-hidden bg-gradient-to-br from-blue-100 to-purple-100 flex-shrink-0">
                <img v-if="currentAlbumDetail.coverUrl" :src="currentAlbumDetail.coverUrl" class="w-full h-full object-cover" />
              </div>
              <div class="flex-1">
                <h2 class="text-2xl font-bold text-gray-800">{{ currentAlbumDetail.name }}</h2>
                <p v-if="currentAlbumDetail.description" class="text-sm text-gray-500 mt-1">{{ currentAlbumDetail.description }}</p>
                <div class="flex items-center space-x-4 mt-2 text-sm text-gray-500">
                  <span>{{ currentAlbumDetail.pictureCount }} 张图片</span>
                  <span>{{ formatSize(currentAlbumDetail.totalSize) }}</span>
                  <span v-if="currentAlbumDetail.lastUploadTime">最近更新: {{ formatTime(currentAlbumDetail.lastUploadTime) }}</span>
                </div>
              </div>
              <button @click="openEditAlbum(currentAlbumDetail)"
                class="px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg text-sm font-medium transition">编辑专辑</button>
            </div>
          </div>
          <div v-if="loading" class="flex justify-center py-20"><div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div></div>
          <div v-else-if="albumPictures.length === 0" class="text-center py-20 bg-white rounded-2xl shadow-sm">
            <div class="text-6xl mb-4">📭</div>
            <h3 class="text-xl font-medium text-gray-500">该专辑暂无图片</h3>
          </div>
          <div v-else class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
            <div v-for="pic in albumPictures" :key="pic.id"
              class="group relative bg-white rounded-2xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100">
              <div class="aspect-square bg-gray-100 relative overflow-hidden cursor-pointer" @click="viewPicture(pic)">
                <img :src="pic.url" class="object-cover w-full h-full transform group-hover:scale-105 transition duration-500" loading="lazy" />
                <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition duration-300">
                  <button @click.stop="removeFromAlbum(pic)"
                    class="p-1.5 bg-white/90 rounded-full text-gray-700 hover:text-red-600 shadow-md transition" title="从专辑移除">
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                    </svg>
                  </button>
                </div>
              </div>
              <div class="p-3">
                <h3 class="text-sm font-medium text-gray-800 truncate">{{ pic.name }}</h3>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- TAGS -->
      <div v-else-if="activeTab === 'tags'">
        <div v-if="!viewingTag">
          <div class="flex items-center justify-between mb-6">
            <div>
              <h2 class="text-2xl font-bold text-gray-800">主题词</h2>
              <p class="text-sm text-gray-500 mt-1">共 {{ allTags.length }} 个主题词</p>
            </div>
            <div class="relative w-64">
              <input v-model="tagSearch" @input="searchTags" type="text" placeholder="搜索主题词..."
                class="w-full pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <svg class="w-4 h-4 absolute left-3 top-2.5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 mb-6">
            <h3 class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-6">主题词云</h3>
            <div v-if="displayedTags.length === 0" class="text-center py-10 text-gray-400">暂无主题词，上传图片时添加主题词即可显示</div>
            <div v-else class="flex flex-wrap items-center justify-center gap-3 min-h-[200px]">
              <button v-for="tag in displayedTags" :key="tag.id" @click="openTagDetail(tag)"
                :style="tagCloudStyle(tag)"
                class="rounded-full px-4 py-2 transition-all duration-200 hover:shadow-lg hover:scale-105 bg-opacity-60">
                #{{ tag.name }}
              </button>
            </div>
          </div>
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="px-5 py-4 border-b border-gray-100 flex items-center justify-between">
              <h3 class="font-semibold text-gray-800">主题词列表</h3>
              <span class="text-xs text-gray-500">按热度排序</span>
            </div>
            <div v-if="displayedTags.length === 0" class="p-10 text-center text-gray-400">暂无主题词</div>
            <div v-else class="divide-y divide-gray-50">
              <div v-for="tag in displayedTags" :key="tag.id"
                class="px-5 py-3 hover:bg-gray-50 transition flex items-center justify-between group">
                <button @click="openTagDetail(tag)" class="flex items-center space-x-4 flex-1 text-left">
                  <span :class="tagRankColor(tag.referenceCount)"
                    class="w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold">
                    {{ tag.referenceCount }}
                  </span>
                  <div>
                    <span class="font-medium text-gray-800">#{{ tag.name }}</span>
                    <p class="text-xs text-gray-400 mt-0.5">创建于 {{ formatTime(tag.createTime) }}</p>
                  </div>
                </button>
                <button v-if="tag.referenceCount === 0" @click="confirmDeleteTag(tag)"
                  class="p-2 text-gray-400 hover:text-red-500 opacity-0 group-hover:opacity-100 transition" title="删除未使用的主题词">
                  <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <div class="mb-6">
            <button @click="viewingTag = false"
              class="inline-flex items-center space-x-1 text-sm text-gray-600 hover:text-blue-600 transition mb-3">
              <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
              </svg>
              <span>返回主题词列表</span>
            </button>
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
              <div class="flex items-center space-x-4">
                <div class="w-16 h-16 rounded-2xl bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center text-white text-2xl font-bold shadow-lg">#</div>
                <div>
                  <h2 class="text-2xl font-bold text-gray-800">{{ currentTagDetail.name }}</h2>
                  <p class="text-sm text-gray-500 mt-1">
                    被引用 <span class="font-semibold text-purple-600">{{ currentTagDetail.referenceCount }}</span> 次
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div v-if="loading" class="flex justify-center py-20"><div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div></div>
          <div v-else-if="tagPictures.length === 0" class="text-center py-20 bg-white rounded-2xl shadow-sm">
            <div class="text-6xl mb-4">🏷️</div>
            <h3 class="text-xl font-medium text-gray-500">暂无使用该主题词的图片</h3>
          </div>
          <div v-else class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
            <div v-for="pic in tagPictures" :key="pic.id"
              class="group relative bg-white rounded-2xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100">
              <div class="aspect-square bg-gray-100 relative overflow-hidden cursor-pointer" @click="viewPicture(pic)">
                <img :src="pic.url" class="object-cover w-full h-full transform group-hover:scale-105 transition duration-500" loading="lazy" />
              </div>
              <div class="p-3">
                <h3 class="text-sm font-medium text-gray-800 truncate">{{ pic.name }}</h3>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- STATS -->
      <div v-else-if="activeTab === 'stats'">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">数据统计</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5 mb-8">
          <div class="bg-gradient-to-br from-blue-500 to-indigo-600 rounded-2xl p-6 text-white shadow-lg">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-blue-100 text-sm">专辑总数</p>
                <p class="text-3xl font-bold mt-1">{{ globalStats.albumCount || 0 }}</p>
              </div>
              <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                </svg>
              </div>
            </div>
          </div>
          <div class="bg-gradient-to-br from-emerald-500 to-teal-600 rounded-2xl p-6 text-white shadow-lg">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-emerald-100 text-sm">图片总数</p>
                <p class="text-3xl font-bold mt-1">{{ globalStats.pictureCount || 0 }}</p>
              </div>
              <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
            </div>
          </div>
          <div class="bg-gradient-to-br from-orange-500 to-amber-600 rounded-2xl p-6 text-white shadow-lg">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-orange-100 text-sm">存储空间</p>
                <p class="text-3xl font-bold mt-1">{{ formatSize(globalStats.totalStorageSize) }}</p>
              </div>
              <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4" />
                </svg>
              </div>
            </div>
          </div>
          <div class="bg-gradient-to-br from-pink-500 to-rose-600 rounded-2xl p-6 text-white shadow-lg">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-pink-100 text-sm">主题词种类</p>
                <p class="text-3xl font-bold mt-1">{{ allTags.length }}</p>
              </div>
              <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
                </svg>
              </div>
            </div>
          </div>
        </div>
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h3 class="font-bold text-gray-800 mb-4 flex items-center space-x-2">
              <svg class="w-5 h-5 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
              <span>专辑图片分布</span>
            </h3>
            <div v-if="allAlbums.length === 0" class="text-center py-10 text-gray-400">暂无专辑数据</div>
            <div v-else class="space-y-3">
              <div v-for="album in allAlbumsSorted" :key="album.id">
                <div class="flex justify-between items-center mb-1">
                  <span class="text-sm font-medium text-gray-700">{{ album.name }}</span>
                  <span class="text-xs text-gray-500">{{ album.pictureCount }} 张 · {{ formatSize(album.totalSize) }}</span>
                </div>
                <div class="w-full h-2 bg-gray-100 rounded-full overflow-hidden">
                  <div class="h-full bg-gradient-to-r from-blue-400 to-purple-500 rounded-full transition-all duration-500"
                    :style="{ width: albumPercentage(album) + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h3 class="font-bold text-gray-800 mb-4 flex items-center space-x-2">
              <svg class="w-5 h-5 text-yellow-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
              </svg>
              <span>热门主题词 TOP 10</span>
            </h3>
            <div v-if="topTenTags.length === 0" class="text-center py-10 text-gray-400">暂无主题词数据</div>
            <div v-else class="space-y-2">
              <div v-for="(tag, idx) in topTenTags" :key="tag.id" class="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-50 transition">
                <span :class="['w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold',
                    idx === 0 ? 'bg-yellow-400 text-white' :
                    idx === 1 ? 'bg-gray-300 text-white' :
                    idx === 2 ? 'bg-amber-600 text-white' : 'bg-gray-100 text-gray-600']">
                  {{ idx + 1 }}
                </span>
                <span class="font-medium text-gray-700 flex-1 cursor-pointer hover:text-blue-600" @click="openTagDetail(tag)">
                  #{{ tag.name }}
                </span>
                <div class="flex items-center space-x-2">
                  <div class="w-24 h-1.5 bg-gray-100 rounded-full overflow-hidden">
                    <div class="h-full bg-gradient-to-r from-purple-400 to-pink-500"
                      :style="{ width: (tag.referenceCount / topRefCount * 100) + '%' }"></div>
                  </div>
                  <span class="text-xs text-gray-500 w-8 text-right">{{ tag.referenceCount }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Upload Modal -->
    <div v-if="showUploadModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/40 backdrop-blur-sm" @click="showUploadModal = false"></div>
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-lg relative transform transition-all max-h-[90vh] overflow-y-auto">
        <div class="p-6 border-b border-gray-100 flex items-center justify-between sticky top-0 bg-white z-10">
          <h3 class="text-lg font-bold text-gray-800">上传图片</h3>
          <button @click="showUploadModal = false" class="text-gray-400 hover:text-gray-600 transition">
            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <div class="p-6 space-y-5">
          <div @click="$refs.uploadFileInput.click()" @dragover.prevent @drop.prevent="handleFileDrop"
            :class="['border-2 border-dashed rounded-xl p-10 text-center cursor-pointer transition-all',
              uploadPreview ? 'border-blue-400 bg-blue-50' : 'border-gray-200 hover:border-blue-300 hover:bg-gray-50']">
            <div v-if="uploadPreview">
              <img :src="uploadPreview" class="max-h-40 mx-auto rounded-lg shadow-md" />
              <p class="text-sm text-gray-500 mt-2">{{ uploadFileName }}</p>
            </div>
            <div v-else>
              <svg class="w-12 h-12 mx-auto text-gray-400 mb-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
              </svg>
              <p class="text-gray-600 font-medium">点击或拖拽图片到此处</p>
              <p class="text-xs text-gray-400 mt-1">支持 JPG, PNG, GIF 等格式，最大 10MB</p>
            </div>
            <input ref="uploadFileInput" type="file" class="hidden" accept="image/*" @change="handleUploadFileSelect" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">选择专辑
              <span class="text-gray-400 text-xs">(可多选，不选则归入默认专辑)</span>
            </label>
            <div class="space-y-2">
              <div class="flex flex-wrap gap-2 max-h-32 overflow-y-auto p-2 bg-gray-50 rounded-lg border border-gray-200">
                <label v-for="a in allAlbums" :key="a.id"
                  :class="['px-3 py-1.5 rounded-lg text-sm cursor-pointer transition border',
                    uploadAlbumIds.includes(a.id)
                      ? 'bg-blue-500 text-white border-blue-500'
                      : 'bg-white text-gray-700 border-gray-200 hover:border-blue-300']">
                  <input type="checkbox" class="hidden" :value="a.id" v-model="uploadAlbumIds" />
                  <span v-if="a.isDefault">📁 </span>{{ a.name }}
                </label>
              </div>
              <button @click="showUploadModal = false; openCreateAlbum(true)"
                class="text-sm text-blue-600 hover:text-blue-700 flex items-center space-x-1">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
                <span>新建专辑</span>
              </button>
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">添加主题词
              <span class="text-gray-400 text-xs">(用逗号分隔，支持多个)</span>
            </label>
            <input v-model="uploadTagInput" type="text" placeholder="例如：风景, 旅行, 春天"
              class="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
            <div v-if="hotTags.length > 0" class="mt-2">
              <p class="text-xs text-gray-500 mb-1.5">热门主题词快速添加：</p>
              <div class="flex flex-wrap gap-1.5">
                <button v-for="t in hotTags.slice(0, 10)" :key="t.id" @click="toggleUploadTag(t.name)"
                  :class="['text-xs px-2.5 py-1 rounded-full transition',
                    uploadTagInput.includes(t.name) ? 'bg-purple-500 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
                  #{{ t.name }}
                </button>
              </div>
            </div>
          </div>
        </div>
        <div class="px-6 py-4 border-t border-gray-100 flex justify-end space-x-3 sticky bottom-0 bg-white">
          <button @click="showUploadModal = false"
            class="px-5 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition font-medium text-sm">取消</button>
          <button @click="doUpload" :disabled="!uploadFile || uploading"
            :class="['px-5 py-2 rounded-lg font-medium text-sm transition',
              uploadFile && !uploading
                ? 'bg-gradient-to-r from-blue-500 to-purple-600 text-white hover:shadow-lg'
                : 'bg-gray-200 text-gray-400 cursor-not-allowed']">
            <span v-if="uploading" class="flex items-center space-x-1.5">
              <svg class="animate-spin h-4 w-4" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" fill="none"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>上传中...</span>
            </span>
            <span v-else>确认上传</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Album Modal -->
    <div v-if="showAlbumModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/40 backdrop-blur-sm" @click="closeAlbumModal"></div>
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md relative transform transition-all">
        <div class="p-6 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-800">{{ editingAlbum ? '编辑专辑' : '新建专辑' }}</h3>
          <button @click="closeAlbumModal" class="text-gray-400 hover:text-gray-600 transition">
            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">专辑名称 <span class="text-red-500">*</span></label>
            <input v-model="albumForm.name" type="text" maxlength="100" placeholder="请输入专辑名称"
              class="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">专辑描述</label>
            <textarea v-model="albumForm.description" maxlength="500" rows="3" placeholder="请输入专辑描述（可选）"
              class="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"></textarea>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">显示顺序（数字越小越靠前）</label>
            <input v-model.number="albumForm.displayOrder" type="number"
              class="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
          </div>
          <label class="flex items-center space-x-2 cursor-pointer">
            <input type="checkbox" v-model="albumForm.isPublic" class="w-4 h-4 text-blue-600 rounded focus:ring-blue-500" />
            <span class="text-sm text-gray-700">公开专辑</span>
          </label>
        </div>
        <div class="px-6 py-4 border-t border-gray-100 flex justify-end space-x-3">
          <button @click="closeAlbumModal"
            class="px-5 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition font-medium text-sm">取消</button>
          <button @click="submitAlbumForm" :disabled="!albumForm.name.trim()"
            :class="['px-5 py-2 rounded-lg font-medium text-sm transition',
              albumForm.name.trim() ? 'bg-blue-500 text-white hover:bg-blue-600' : 'bg-gray-200 text-gray-400 cursor-not-allowed']">
            确认
          </button>
        </div>
      </div>
    </div>

    <!-- Picture Detail Modal -->
    <div v-if="showPictureDetail" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60" @click="closePictureDetail"></div>
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-4xl max-h-[90vh] overflow-hidden relative flex flex-col lg:flex-row">
        <button @click="closePictureDetail"
          class="absolute top-4 right-4 z-10 p-2 bg-white/90 rounded-full text-gray-600 hover:bg-white transition shadow-md">
          <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
        <div class="lg:w-1/2 bg-gray-900 flex items-center justify-center p-4 min-h-[300px] lg:min-h-0">
          <img :src="currentPicture.url" class="max-w-full max-h-[50vh] lg:max-h-[80vh] object-contain rounded-lg" />
        </div>
        <div class="lg:w-1/2 p-6 overflow-y-auto">
          <h3 class="text-xl font-bold text-gray-800 mb-1">{{ currentPicture.name }}</h3>
          <p class="text-xs text-gray-500 mb-4">
            {{ formatTime(currentPicture.createTime) }} · {{ formatSize(currentPicture.size) }}
          </p>
          <div class="mb-5">
            <div class="flex items-center justify-between mb-2">
              <label class="text-sm font-semibold text-gray-700">所属专辑</label>
              <button v-if="!editingPictureFields" @click="startEditPictureFields"
                class="text-xs text-blue-600 hover:text-blue-700 flex items-center space-x-0.5">
                <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
                <span>编辑</span>
              </button>
            </div>
            <div v-if="!editingPictureFields">
              <div v-if="(currentPicture.albums || []).length === 0" class="text-xs text-gray-400">未归属任何专辑</div>
              <div v-else class="flex flex-wrap gap-1.5">
                <span v-for="a in currentPicture.albums" :key="a.id"
                  class="inline-flex items-center space-x-1 px-2.5 py-1 bg-blue-50 text-blue-700 rounded-full text-xs cursor-pointer hover:bg-blue-100 transition"
                  @click="navigateToAlbum(a.id)">
                  <span v-if="a.isDefault">📁 </span>{{ a.name }}
                </span>
              </div>
            </div>
            <div v-else class="space-y-2">
              <div class="flex flex-wrap gap-1.5 max-h-32 overflow-y-auto p-2 bg-gray-50 rounded-lg border border-gray-200">
                <label v-for="a in allAlbums" :key="a.id"
                  :class="['px-2.5 py-1 rounded-lg text-xs cursor-pointer transition border',
                    editPictureAlbumIds.includes(a.id)
                      ? 'bg-blue-500 text-white border-blue-500'
                      : 'bg-white text-gray-700 border-gray-200 hover:border-blue-300']">
                  <input type="checkbox" class="hidden" :value="a.id" v-model="editPictureAlbumIds" />
                  <span v-if="a.isDefault">📁 </span>{{ a.name }}
                </label>
              </div>
            </div>
          </div>
          <div class="mb-5">
            <div class="flex items-center justify-between mb-2">
              <label class="text-sm font-semibold text-gray-700">主题词</label>
              <button v-if="!editingPictureFields" @click="startEditPictureFields"
                class="text-xs text-blue-600 hover:text-blue-700 flex items-center space-x-0.5">
                <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
                <span>新增/编辑</span>
              </button>
            </div>
            <div v-if="!editingPictureFields">
              <div v-if="(currentPicture.tags || []).length === 0" class="text-xs text-gray-400">暂无主题词</div>
              <div v-else class="flex flex-wrap gap-1.5">
                <span v-for="t in currentPicture.tags" :key="t.id"
                  class="inline-flex items-center space-x-1 px-2.5 py-1 bg-purple-50 text-purple-700 rounded-full text-xs cursor-pointer hover:bg-purple-100 transition"
                  @click="navigateToTag(t)">
                  #{{ t.name }}
                </span>
              </div>
            </div>
            <div v-else class="space-y-2">
              <input v-model="editPictureTagInput" type="text" placeholder="输入主题词，多个用逗号分隔"
                class="w-full px-3 py-2 border border-gray-200 rounded-lg text-xs focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <div v-if="hotTags.length > 0" class="flex flex-wrap gap-1">
                <button v-for="t in hotTags.slice(0, 10)" :key="t.id" @click="toggleEditPictureTag(t.name)"
                  :class="['text-[10px] px-2 py-0.5 rounded-full transition',
                    editPictureTagInput.includes(t.name)
                      ? 'bg-purple-500 text-white'
                      : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
                  #{{ t.name }}
                </button>
              </div>
            </div>
          </div>
          <div v-if="editingPictureFields" class="flex space-x-2 pt-2">
            <button @click="savePictureFields"
              class="flex-1 px-4 py-2 bg-blue-500 text-white rounded-lg text-sm font-medium hover:bg-blue-600 transition">保存修改</button>
            <button @click="cancelEditPictureFields"
              class="flex-1 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg text-sm font-medium hover:bg-gray-200 transition">取消</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Batch Tag Modal -->
    <div v-if="showBatchTagModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/40 backdrop-blur-sm" @click="showBatchTagModal = false"></div>
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md relative transform transition-all">
        <div class="p-6 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-800">批量添加主题词</h3>
          <button @click="showBatchTagModal = false" class="text-gray-400 hover:text-gray-600 transition">
            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <div class="p-6 space-y-4">
          <p class="text-sm text-gray-500">已选择 {{ selectedPictureIds.length }} 张图片，将为它们添加以下主题词：</p>
          <input v-model="batchTagInput" type="text" placeholder="输入主题词，多个用逗号分隔（如：风景, 旅行）"
            class="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500" />
          <div v-if="hotTags.length > 0" class="flex flex-wrap gap-1.5">
            <button v-for="t in hotTags.slice(0, 10)" :key="t.id" @click="toggleBatchTag(t.name)"
              :class="['text-xs px-2.5 py-1 rounded-full transition',
                batchTagInput.includes(t.name) ? 'bg-emerald-500 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
              #{{ t.name }}
            </button>
          </div>
        </div>
        <div class="px-6 py-4 border-t border-gray-100 flex justify-end space-x-3">
          <button @click="showBatchTagModal = false"
            class="px-5 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition font-medium text-sm">取消</button>
          <button @click="doBatchTag" :disabled="!batchTagInput.trim()"
            :class="['px-5 py-2 rounded-lg font-medium text-sm transition',
              batchTagInput.trim() ? 'bg-emerald-500 text-white hover:bg-emerald-600' : 'bg-gray-200 text-gray-400 cursor-not-allowed']">
            确认添加
          </button>
        </div>
      </div>
    </div>

    <!-- Batch Album Modal -->
    <div v-if="showBatchAlbumModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/40 backdrop-blur-sm" @click="showBatchAlbumModal = false"></div>
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md relative transform transition-all">
        <div class="p-6 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-800">批量归入专辑</h3>
          <button @click="showBatchAlbumModal = false" class="text-gray-400 hover:text-gray-600 transition">
            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <div class="p-6 space-y-4">
          <p class="text-sm text-gray-500">已选择 {{ selectedPictureIds.length }} 张图片，将添加到：</p>
          <select v-model="batchAlbumId" class="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option :value="null">请选择专辑...</option>
            <option v-for="a in allAlbums" :key="a.id" :value="a.id">{{ a.name }} ({{ a.pictureCount }} 张)</option>
          </select>
        </div>
        <div class="px-6 py-4 border-t border-gray-100 flex justify-end space-x-3">
          <button @click="showBatchAlbumModal = false"
            class="px-5 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition font-medium text-sm">取消</button>
          <button @click="doBatchAlbum" :disabled="!batchAlbumId"
            :class="['px-5 py-2 rounded-lg font-medium text-sm transition',
              batchAlbumId ? 'bg-blue-500 text-white hover:bg-blue-600' : 'bg-gray-200 text-gray-400 cursor-not-allowed']">
            确认添加
          </button>
        </div>
      </div>
    </div>

    <!-- Generic Confirm Modal -->
    <div v-if="confirmModal.show" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/40 backdrop-blur-sm" @click="confirmModal.show = false"></div>
      <div class="bg-white rounded-2xl shadow-2xl max-w-sm w-full relative transform transition-all">
        <div class="p-6">
          <div class="flex items-start space-x-4">
            <div :class="['mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full',
                confirmModal.type === 'danger' ? 'bg-red-100' :
                confirmModal.type === 'warning' ? 'bg-yellow-100' : 'bg-blue-100']">
              <svg :class="['h-6 w-6',
                  confirmModal.type === 'danger' ? 'text-red-600' :
                  confirmModal.type === 'warning' ? 'text-yellow-600' : 'text-blue-600']"
                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
            </div>
            <div class="mt-0 text-center flex-1">
              <h3 class="text-lg font-semibold text-gray-900">{{ confirmModal.title }}</h3>
              <p class="text-sm text-gray-500 mt-2">{{ confirmModal.message }}</p>
              <div v-if="confirmModal.options" class="mt-4 space-y-2 text-left">
                <label v-for="opt in confirmModal.options" :key="opt.value"
                  class="flex items-center space-x-2 p-2 rounded-lg cursor-pointer hover:bg-gray-50 transition"
                  :class="confirmModal.selectedOption === opt.value ? 'bg-blue-50 ring-1 ring-blue-300' : ''"
                  @click="confirmModal.selectedOption = opt.value">
                  <input type="radio" v-model="confirmModal.selectedOption" :value="opt.value" class="text-blue-600" />
                  <div>
                    <p class="text-sm font-medium text-gray-800">{{ opt.label }}</p>
                    <p class="text-xs text-gray-500">{{ opt.description }}</p>
                  </div>
                </label>
              </div>
            </div>
          </div>
        </div>
        <div class="px-6 py-4 border-t border-gray-100 flex space-x-3">
          <button @click="confirmModal.show = false"
            class="flex-1 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition font-medium">取消</button>
          <button @click="confirmModal.onConfirm"
            :class="['flex-1 px-4 py-2 rounded-lg font-medium text-white transition shadow-md',
                confirmModal.type === 'danger' ? 'bg-red-500 hover:bg-red-600' :
                confirmModal.type === 'warning' ? 'bg-yellow-500 hover:bg-yellow-600' : 'bg-blue-500 hover:bg-blue-600']">
            确认
          </button>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <transition enter-active-class="transform ease-out duration-300 transition" enter-from-class="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
      enter-to-class="translate-y-0 opacity-100 sm:translate-x-0" leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100" leave-to-class="opacity-0">
      <div v-if="toast.show" class="fixed top-20 right-5 z-50 max-w-sm w-full bg-white shadow-lg rounded-lg pointer-events-auto ring-1 ring-black ring-opacity-5 overflow-hidden">
        <div class="p-4">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <svg v-if="toast.type === 'success'" class="h-6 w-6 text-green-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <svg v-else class="h-6 w-6 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="ml-3 w-0 flex-1 pt-0.5">
              <p class="text-sm font-medium text-gray-900">{{ toast.message }}</p>
            </div>
            <div class="ml-4 flex-shrink-0 flex">
              <button @click="toast.show = false" class="bg-white rounded-md inline-flex text-gray-400 hover:text-gray-500 focus:outline-none">
                <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

const tabs = [
  { id: 'pictures', name: '图片墙' },
  { id: 'albums', name: '专辑' },
  { id: 'tags', name: '主题词' },
  { id: 'stats', name: '统计' }
]

const activeTab = ref('pictures')
const loading = ref(false)
const globalSearch = ref('')

// Global data
const pictures = ref([])
const allAlbums = ref([])
const allTags = ref([])
const globalStats = ref({})

// Filter / select
const filterAlbumId = ref(null)
const filterTagId = ref(null)
const multiSelectMode = ref(false)
const selectedPictureIds = ref([])

// Toast
const toast = reactive({ show: false, message: '', type: 'success' })
const showToast = (message, type = 'success') => {
  toast.show = true; toast.message = message; toast.type = type
  setTimeout(() => toast.show = false, 3000)
}

// Confirm modal
const confirmModal = reactive({
  show: false, title: '', message: '', type: 'danger',
  options: null, selectedOption: null, onConfirm: () => {}
})
const openConfirm = (config) => {
  Object.assign(confirmModal, { show: true, options: null, selectedOption: null, ...config })
}

// Utility formatters
const formatSize = (bytes) => {
  if (!bytes || bytes === 0) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let i = 0, b = Number(bytes)
  while (b >= 1024 && i < units.length - 1) { b /= 1024; i++ }
  return b.toFixed(i > 0 ? 2 : 0) + ' ' + units[i]
}
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const d = new Date(timeStr)
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}

// Upload
const showUploadModal = ref(false)
const uploadFile = ref(null)
const uploadPreview = ref('')
const uploadFileName = ref('')
const uploadAlbumIds = ref([])
const uploadTagInput = ref('')
const uploading = ref(false)
let reopenUploadAfterAlbum = false

const handleUploadFileSelect = (e) => {
  const f = e.target.files[0]
  if (!f) return
  uploadFile.value = f
  uploadFileName.value = f.name
  const reader = new FileReader()
  reader.onload = (ev) => uploadPreview.value = ev.target.result
  reader.readAsDataURL(f)
}
const handleFileDrop = (e) => {
  const f = e.dataTransfer.files[0]
  if (!f || !f.type.startsWith('image/')) return
  uploadFile.value = f
  uploadFileName.value = f.name
  const reader = new FileReader()
  reader.onload = (ev) => uploadPreview.value = ev.target.result
  reader.readAsDataURL(f)
}
const resetUploadForm = () => {
  uploadFile.value = null; uploadPreview.value = ''; uploadFileName.value = ''
  uploadAlbumIds.value = []; uploadTagInput.value = ''; uploading.value = false
}
const toggleUploadTag = (name) => {
  const cur = uploadTagInput.value.split(/[,，]/).map(s => s.trim()).filter(Boolean)
  const idx = cur.indexOf(name)
  if (idx >= 0) cur.splice(idx, 1); else cur.push(name)
  uploadTagInput.value = cur.join(', ')
}
const doUpload = async () => {
  if (!uploadFile.value) return
  uploading.value = true
  try {
    const fd = new FormData()
    fd.append('file', uploadFile.value)
    uploadAlbumIds.value.forEach(id => fd.append('albumIds', id))
    const tagList = uploadTagInput.value.split(/[,，]/).map(s => s.trim()).filter(Boolean)
    tagList.forEach(t => fd.append('tags', t))
    const res = await api.post('/upload', fd)
    if (res.data.success) {
      showToast('上传成功！')
      showUploadModal.value = false
      resetUploadForm()
      await fetchAll()
    } else {
      showToast(res.data.message || '上传失败', 'error')
    }
  } catch (e) {
    console.error(e); showToast('上传失败', 'error')
  } finally {
    uploading.value = false
  }
}

// Album
const viewingAlbum = ref(false)
const currentAlbumDetail = ref({})
const albumPictures = ref([])
const showAlbumModal = ref(false)
const editingAlbum = ref(null)
const albumForm = reactive({ name: '', description: '', isPublic: true, displayOrder: 0 })
const openCreateAlbum = (reopenUpload = false) => {
  reopenUploadAfterAlbum = reopenUpload
  editingAlbum.value = null
  albumForm.name = ''; albumForm.description = ''; albumForm.isPublic = true; albumForm.displayOrder = 0
  showAlbumModal.value = true
}
const openEditAlbum = (album) => {
  editingAlbum.value = album
  albumForm.name = album.name || ''
  albumForm.description = album.description || ''
  albumForm.isPublic = album.isPublic ?? true
  albumForm.displayOrder = album.displayOrder ?? 0
  showAlbumModal.value = true
}
const closeAlbumModal = () => {
  showAlbumModal.value = false
  if (reopenUploadAfterAlbum) { showUploadModal.value = true; reopenUploadAfterAlbum = false }
}
const submitAlbumForm = async () => {
  try {
    let res
    if (editingAlbum.value) {
      res = await api.put(`/albums/${editingAlbum.value.id}`, { ...albumForm })
    } else {
      res = await api.post('/albums', { ...albumForm })
    }
    if (res.data.success) {
      showToast(editingAlbum.value ? '专辑已更新' : '专辑已创建')
      showAlbumModal.value = false
      if (reopenUploadAfterAlbum) { showUploadModal.value = true; reopenUploadAfterAlbum = false }
      await fetchAlbums()
    } else {
      showToast(res.data.message || '操作失败', 'error')
    }
  } catch (e) { showToast('网络错误', 'error') }
}
const openAlbumDetail = async (album) => {
  currentAlbumDetail.value = album
  viewingAlbum.value = true
  loading.value = true
  try {
    const res = await api.get(`/albums/${album.id}/pictures`)
    if (res.data.success) albumPictures.value = res.data.data || []
  } catch (e) {}
  finally { loading.value = false }
}
const getAlbumCover = (album) => {
  if (album.coverUrl) return album.coverUrl
  if (album.coverPictureId) {
    const pic = pictures.value.find(p => p.id === album.coverPictureId)
    return pic ? pic.url : ''
  }
  return ''
}
const confirmDeleteAlbum = (album) => {
  openConfirm({
    title: `删除专辑"${album.name}"?`,
    message: '请选择删除方式：',
    type: 'danger',
    options: [
      { value: 'move', label: '仅删除专辑', description: '图片将移至"默认专辑"' },
      { value: 'delete', label: '删除专辑及图片', description: '所有图片将被永久删除（不可恢复）' }
    ],
    selectedOption: 'move',
    onConfirm: async () => {
      try {
        const mode = confirmModal.selectedOption
        const res = await api.delete(`/albums/${album.id}?deletePictures=${mode === 'delete'}`)
        if (res.data.success) {
          showToast('专辑已删除')
          confirmModal.show = false
          await fetchAll()
        } else showToast(res.data.message || '删除失败', 'error')
      } catch (e) { showToast('删除失败', 'error') }
    }
  })
}
const removeFromAlbum = (pic) => {
  openConfirm({
    title: `从"${currentAlbumDetail.value.name}"移除图片？`,
    message: '图片本身不会被删除，将从该专辑中移除。',
    type: 'warning',
    onConfirm: async () => {
      try {
        const res = await api.delete(`/albums/${currentAlbumDetail.value.id}/pictures/${pic.id}`)
        if (res.data.success) {
          showToast('已从专辑移除')
          confirmModal.show = false
          await openAlbumDetail(currentAlbumDetail.value)
          await fetchAlbums()
        } else showToast(res.data.message || '操作失败', 'error')
      } catch (e) { showToast('操作失败', 'error') }
    }
  })
}
const navigateToAlbum = (id) => {
  const album = allAlbums.value.find(a => a.id === id)
  if (album) {
    showPictureDetail.value = false
    activeTab.value = 'albums'
    openAlbumDetail(album)
  }
}
const allAlbumsSorted = computed(() =>
  [...allAlbums.value].sort((a, b) => (b.pictureCount || 0) - (a.pictureCount || 0))
)
const albumPercentage = (album) => {
  const max = Math.max(...allAlbums.value.map(a => a.pictureCount || 0), 1)
  return Math.round((album.pictureCount || 0) / max * 100)
}

// Tags
const viewingTag = ref(false)
const currentTagDetail = ref({})
const tagPictures = ref([])
const tagSearch = ref('')
const displayedTags = computed(() => {
  const kw = tagSearch.value.trim().toLowerCase()
  if (!kw) return allTags.value
  return allTags.value.filter(t => t.name.toLowerCase().includes(kw))
})
const hotTags = computed(() =>
  [...allTags.value].sort((a, b) => (b.referenceCount || 0) - (a.referenceCount || 0)).slice(0, 20)
)
const topTenTags = computed(() =>
  [...allTags.value].sort((a, b) => (b.referenceCount || 0) - (a.referenceCount || 0)).slice(0, 10)
)
const topRefCount = computed(() => {
  const t = topTenTags.value[0]
  return t ? (t.referenceCount || 1) : 1
})
const tagCloudStyle = (tag) => {
  const base = 12
  const max = 32
  const ratio = Math.min((tag.referenceCount || 0) / Math.max(topRefCount.value, 1), 1)
  const size = base + ratio * (max - base)
  const colors = ['#2563EB', '#9333EA', '#DB2777', '#059669', '#D97706', '#E11D48', '#4F46E5', '#0D9488']
  const bgColors = ['#DBEAFE', '#F3E8FF', '#FCE7F3', '#D1FAE5', '#FEF3C7', '#FFE4E6', '#E0E7FF', '#CCFBF1']
  const idx = (tag.id || 0) % colors.length
  return {
    fontSize: size + 'px',
    fontWeight: 500 + Math.round(ratio * 3) * 100,
    color: colors[idx],
    backgroundColor: bgColors[idx]
  }
}
const tagRankColor = (count) => {
  if (count >= 10) return 'bg-gradient-to-br from-pink-500 to-rose-600 text-white'
  if (count >= 5) return 'bg-gradient-to-br from-purple-500 to-indigo-600 text-white'
  if (count >= 3) return 'bg-gradient-to-br from-blue-500 to-cyan-600 text-white'
  if (count >= 1) return 'bg-gradient-to-br from-emerald-500 to-teal-600 text-white'
  return 'bg-gray-200 text-gray-500'
}
const searchTags = () => {}
const openTagDetail = async (tag) => {
  currentTagDetail.value = tag
  viewingTag.value = true
  loading.value = true
  try {
    const res = await api.get(`/tags/${tag.id}/pictures`)
    if (res.data.success) tagPictures.value = res.data.data || []
  } catch (e) {} finally { loading.value = false }
}
const navigateToTag = (tag) => {
  showPictureDetail.value = false
  activeTab.value = 'tags'
  openTagDetail(tag)
}
const confirmDeleteTag = (tag) => {
  openConfirm({
    title: `删除主题词"#${tag.name}"?`,
    message: '该主题词未被使用，可以安全删除。',
    type: 'danger',
    onConfirm: async () => {
      try {
        const res = await api.delete(`/tags/${tag.id}`)
        if (res.data.success) {
          showToast('主题词已删除')
          confirmModal.show = false
          await fetchTags()
        } else showToast(res.data.message || '删除失败', 'error')
      } catch (e) { showToast('删除失败', 'error') }
    }
  })
}

// Picture Detail
const showPictureDetail = ref(false)
const currentPicture = ref({})
const editingPictureFields = ref(false)
const editPictureAlbumIds = ref([])
const editPictureTagInput = ref('')

const viewPicture = async (pic) => {
  loading.value = true
  try {
    const res = await api.get(`/pictures/${pic.id}`)
    if (res.data.success) {
      currentPicture.value = res.data.data
      showPictureDetail.value = true
    }
  } catch (e) {} finally { loading.value = false }
}
const closePictureDetail = () => {
  showPictureDetail.value = false
  editingPictureFields.value = false
}
const startEditPictureFields = () => {
  editingPictureFields.value = true
  editPictureAlbumIds.value = (currentPicture.value.albums || []).map(a => a.id)
  editPictureTagInput.value = (currentPicture.value.tags || []).map(t => t.name).join(', ')
}
const cancelEditPictureFields = () => {
  editingPictureFields.value = false
  editPictureAlbumIds.value = []
  editPictureTagInput.value = ''
}
const toggleEditPictureTag = (name) => {
  const cur = editPictureTagInput.value.split(/[,，]/).map(s => s.trim()).filter(Boolean)
  const idx = cur.indexOf(name)
  if (idx >= 0) cur.splice(idx, 1); else cur.push(name)
  editPictureTagInput.value = cur.join(', ')
}
const savePictureFields = async () => {
  try {
    const tagNames = editPictureTagInput.value.split(/[,，]/).map(s => s.trim()).filter(Boolean)
    const res = await api.put(`/pictures/${currentPicture.value.id}`, {
      albumIds: editPictureAlbumIds.value,
      tagNames: tagNames
    })
    if (res.data.success) {
      showToast('图片信息已更新')
      editingPictureFields.value = false
      currentPicture.value = res.data.data
      await fetchAll()
    } else showToast(res.data.message || '保存失败', 'error')
  } catch (e) { showToast('保存失败', 'error') }
}
const confirmDeletePicture = (pic) => {
  openConfirm({
    title: `删除图片"${pic.name}"?`,
    message: '此操作将永久删除图片及所有关联关系，不可恢复。',
    type: 'danger',
    onConfirm: async () => {
      try {
        const res = await api.delete(`/delete/${pic.id}`)
        if (res.data.success) {
          showToast('图片已删除')
          confirmModal.show = false
          await fetchAll()
        } else showToast(res.data.message || '删除失败', 'error')
      } catch (e) { showToast('删除失败', 'error') }
    }
  })
}

// Batch operations
const showBatchTagModal = ref(false)
const batchTagInput = ref('')
const toggleBatchTag = (name) => {
  const cur = batchTagInput.value.split(/[,，]/).map(s => s.trim()).filter(Boolean)
  const idx = cur.indexOf(name)
  if (idx >= 0) cur.splice(idx, 1); else cur.push(name)
  batchTagInput.value = cur.join(', ')
}
const doBatchTag = async () => {
  if (!batchTagInput.value.trim() || selectedPictureIds.value.length === 0) return
  try {
    const tagNames = batchTagInput.value.split(/[,，]/).map(s => s.trim()).filter(Boolean)
    const res = await api.post('/pictures/batch-tags', {
      pictureIds: selectedPictureIds.value,
      tagNames: tagNames
    })
    if (res.data.success) {
      showToast(`已为 ${selectedPictureIds.value.length} 张图片添加主题词`)
      showBatchTagModal.value = false
      batchTagInput.value = ''
      selectedPictureIds.value = []
      await fetchAll()
    } else showToast(res.data.message || '操作失败', 'error')
  } catch (e) { showToast('操作失败', 'error') }
}

const showBatchAlbumModal = ref(false)
const batchAlbumId = ref(null)
const doBatchAlbum = async () => {
  if (!batchAlbumId.value || selectedPictureIds.value.length === 0) return
  try {
    const res = await api.post('/pictures/batch-album', {
      pictureIds: selectedPictureIds.value,
      albumId: batchAlbumId.value
    })
    if (res.data.success) {
      showToast(`已为 ${selectedPictureIds.value.length} 张图片添加到专辑`)
      showBatchAlbumModal.value = false
      batchAlbumId.value = null
      selectedPictureIds.value = []
      await fetchAll()
    } else showToast(res.data.message || '操作失败', 'error')
  } catch (e) { showToast('操作失败', 'error') }
}

const confirmBatchDelete = () => {
  openConfirm({
    title: `批量删除 ${selectedPictureIds.value.length} 张图片？`,
    message: '此操作将永久删除选中的图片及其所有关联关系，不可恢复。',
    type: 'danger',
    onConfirm: async () => {
      try {
        const res = await api.post('/pictures/batch-delete', { pictureIds: selectedPictureIds.value })
        if (res.data.success) {
          showToast(`已删除 ${selectedPictureIds.value.length} 张图片`)
          confirmModal.show = false
          selectedPictureIds.value = []
          multiSelectMode.value = false
          await fetchAll()
        } else showToast(res.data.message || '删除失败', 'error')
      } catch (e) { showToast('删除失败', 'error') }
    }
  })
}

// Global search
const doGlobalSearch = async () => {
  const kw = globalSearch.value.trim()
  if (!kw) { filterAlbumId.value = null; filterTagId.value = null; activeTab.value = 'pictures'; fetchPictures(); return }
  activeTab.value = 'pictures'
  filterAlbumId.value = null; filterTagId.value = null
  loading.value = true
  try {
    const res = await api.get(`/list?keyword=${encodeURIComponent(kw)}`)
    if (res.data.success) pictures.value = res.data.data || []
  } catch (e) {} finally { loading.value = false }
}

// Fetchers
const fetchPictures = async () => {
  loading.value = true
  try {
    let url = '/list'
    const q = []
    if (filterAlbumId.value) q.push(`albumId=${filterAlbumId.value}`)
    if (filterTagId.value) q.push(`tagId=${filterTagId.value}`)
    if (q.length > 0) url += '?' + q.join('&')
    const res = await api.get(url)
    if (res.data.success) pictures.value = res.data.data || []
  } catch (e) { console.error(e); showToast('加载失败', 'error') }
  finally { loading.value = false }
}
const fetchAlbums = async () => {
  try {
    const res = await api.get('/albums')
    if (res.data.success) allAlbums.value = res.data.data || []
  } catch (e) {}
}
const fetchTags = async () => {
  try {
    const res = await api.get('/tags')
    if (res.data.success) allTags.value = res.data.data || []
  } catch (e) {}
}
const fetchStats = async () => {
  try {
    const res = await api.get('/stats')
    if (res.data.success) globalStats.value = res.data.data || {}
  } catch (e) {}
}
const fetchAll = async () => {
  await Promise.all([fetchPictures(), fetchAlbums(), fetchTags(), fetchStats()])
}

// Watch tab changes
watch(activeTab, async (newVal) => {
  if (newVal === 'pictures' && pictures.value.length === 0) await fetchPictures()
  if (newVal === 'albums') await fetchAlbums()
  if (newVal === 'tags') await fetchTags()
  if (newVal === 'stats') { await fetchAlbums(); await fetchTags(); await fetchStats() }
})

onMounted(() => fetchAll())
</script>

<style>
::-webkit-scrollbar { width: 8px; height: 8px; }
::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 4px; }
::-webkit-scrollbar-thumb { background: #c1c1c1; border-radius: 4px; }
::-webkit-scrollbar-thumb:hover { background: #a8a8a8; }
.line-clamp-2 { display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
</style>
