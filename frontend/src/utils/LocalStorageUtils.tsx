
class LocalStorageUtils
{
    public static setItem(key:string, value: string)
    {
        localStorage.setItem(key, value);
    }

    public static getItem(key:string): string | null
    {
        return localStorage.getItem(key);
    }
}

export default LocalStorageUtils;