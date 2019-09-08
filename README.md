# LRUCache
Implemented LRUCache

High Level Features:

1. LRUCache follows least recently used eviction policy.
2. In this project the cache files are stored in disks.
3. For each Key there could be multiple files and they can be accessed with the index.
The files have been named as " Cahche-<Key #value>.#index value
4. User can initialize the cache by providing the fixed size, the max no of files can be stored for a same key and the cache directory.
5. If the cache is full, the lease recently used Key will be identified and all the files will be removed in the background thread
until the cache size is not reduced below maxSize.
6. Multiple Threads can read and write into the cache simultenously. The locking mechanism will take care of the synchronization.
7. The code is pluggable. Any type of cache and eviction policy can be implemented.


