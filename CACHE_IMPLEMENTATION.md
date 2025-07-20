# Repository Caching Implementation

## Overview
This implementation adds comprehensive local caching for API responses in the HackerFeed Android application. The caching system improves performance, reduces network usage, and provides offline capabilities.

## Architecture

### 1. Database Layer
New entities and DAOs were added to manage cached data:
- **`CachedArticle`**: Stores individual articles with cache timestamps
- **`CachedTopStories`**: Stores top story IDs as JSON with cache timestamps
- **`CachedArticleDao`**: Data access methods for cached articles
- **`CachedTopStoriesDao`**: Data access methods for cached top stories

### 2. Cache Management
- **`CacheConfig`**: Configuration constants for cache durations and limits
  - Top stories cache: 5 minutes
  - Article cache: 30 minutes
  - Max cached articles: 1000
- **`CacheManager`**: Core cache operations with memory and database caching

### 3. Data Sources
- **`LocalNewsDataSource`**: Interface for local news data operations
- **`RoomNewsDataSource`**: Implementation using Room database

### 4. Repository Updates
- **`NewsRepositoryImpl`**: Enhanced with cache-first strategy
  - Check cache first, fall back to network
  - Cache network responses for future use
  - Provide expired cache as fallback during network errors

### 5. Use Cases
- **`ClearCacheUseCase`**: Clear all cached data
- **`ClearExpiredCacheUseCase`**: Clear only expired cache entries

### 6. Presentation Layer
- **`CacheViewModel`**: Manages cache operations in the UI
- **`CacheManagementActivity`**: User interface for cache management

## Features Implemented

### 1. Multi-Level Caching
- **Memory Cache**: Fast access for frequently used data (top stories)
- **Database Cache**: Persistent storage for offline access
- **Intelligent Fallback**: Uses expired cache when network fails

### 2. Cache Strategy
- **Cache-First**: Always check cache before network requests
- **Background Cleanup**: Automatic cleanup of expired entries
- **Size Management**: Automatic cleanup when cache exceeds limits

### 3. Cache Configuration
- **Configurable Durations**: Different expiration times for different data types
- **Size Limits**: Prevents unlimited cache growth
- **Cleanup Thresholds**: Efficient cache maintenance

### 4. User Management
- **Cache Management UI**: Dedicated screen for cache operations
- **Clear Cache**: Option to clear all cached data
- **Clear Expired**: Option to clear only expired cache
- **Status Feedback**: User feedback on cache operations

## Usage

### Automatic Caching
The caching system works automatically:
1. App requests data
2. Cache is checked first
3. If cache miss, network request is made
4. Network response is cached for future use
5. Expired cache cleanup happens in background

### Manual Cache Management
Users can manage cache through the Cache Management screen:
- Access via storage icon in the main app bar
- Clear expired cache to free space while keeping recent data
- Clear all cache to reset completely

### Developer Benefits
1. **Improved Performance**: Faster data access from local cache
2. **Reduced Network Usage**: Fewer API calls due to caching
3. **Offline Support**: App works with cached data when offline
4. **Better UX**: Instant loading of cached content

## Cache Behavior

### Top Stories
- Cached for 5 minutes
- Stored in both memory and database
- Refreshed on pull-to-refresh

### Articles
- Cached for 30 minutes
- Database storage only
- Batch fetching optimizes cache usage

### Cache Cleanup
- Automatic cleanup on app start
- Size-based cleanup when limit exceeded
- User-initiated cleanup through UI

## Dependencies Added
- **kotlinx-serialization-json**: For JSON serialization of top stories
- **kotlinx-coroutines**: For async cache operations (if not already present)

## Database Migration
- Database version upgraded from 1 to 2
- Migration script handles adding new cache tables
- Existing favorites data is preserved

## Configuration Options
All cache behavior can be adjusted in `CacheConfig`:
```kotlin
const val TOP_STORIES_CACHE_DURATION = 5 * 60 * 1000L // 5 minutes
const val ARTICLE_CACHE_DURATION = 30 * 60 * 1000L // 30 minutes
const val MAX_CACHED_ARTICLES = 1000
```

## Error Handling
- Cache errors don't affect app functionality
- Graceful fallback to network when cache fails
- Silent cleanup errors to avoid disrupting UX

## Future Enhancements
1. **Cache Analytics**: Track cache hit/miss rates
2. **Smart Prefetching**: Preload likely-to-be-accessed content
3. **Compression**: Compress cached data to save space
4. **Cache Warming**: Background refresh of popular content
5. **User Preferences**: Allow users to configure cache behavior

## Testing
The implementation includes:
- Unit tests can be added for cache logic
- Integration tests for database operations
- UI tests for cache management screen

This caching implementation significantly improves the app's performance and user experience while providing a solid foundation for future enhancements.
