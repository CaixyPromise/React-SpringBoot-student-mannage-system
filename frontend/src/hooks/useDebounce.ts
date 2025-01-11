import { useCallback, useEffect, useRef } from 'react';

function useDebounce<T extends (...args: any[]) => Promise<any> | any>(
    callback: T,
    delay: number
): [T, () => void] {
    const timeoutRef = useRef<NodeJS.Timeout | null>(null);

    // 清理函数
    const cancel = useCallback(() => {
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
            timeoutRef.current = null;
        }
    }, []);

    // 清理组件卸载时的 timeout
    useEffect(() => {
        return cancel;
    }, [cancel]);

    // 生成防抖动处理函数
    const debouncedCallback = useCallback(
        (...args: Parameters<T>): Promise<ReturnType<T>> => {
            cancel();

            return new Promise((resolve, reject) => {
                timeoutRef.current = setTimeout(async () => {
                    try {
                        const result = await callback(...args);
                        resolve(result);
                    } catch (error) {
                        reject(error);
                    }
                }, delay);
            });
        },
        [callback, delay, cancel]
    );

    return [debouncedCallback as T, cancel];
}

export default useDebounce;
