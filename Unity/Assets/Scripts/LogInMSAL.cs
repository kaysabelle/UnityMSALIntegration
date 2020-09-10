using UnityEngine;
using UnityEngine.UI;

public class LogInMSAL : MonoBehaviour
{
    public Button LoginButton;
    public Button LogoutButton;

    // Name of the Android library plugin
    const string pluginName = "com.example.msal.MsalWrapper";
    private AndroidJavaObject pluginClass;

    public AndroidJavaObject PluginClass
    {
        get
        {
            if (pluginClass == null)
            {
                pluginClass = new AndroidJavaObject(pluginName);
            }

            return pluginClass;
        }
    }

    class AndroidPluginCallback : AndroidJavaProxy
    {
        public AndroidPluginCallback() : base("com.example.msal.PluginCallback") { }

        public void onSuccess(string token)
        {
            Debug.Log("TOKEN IS: " + token);
        }

        public void onError(string message)
        {
            Debug.LogError("ERROR IS: " + message);
        }
    }

    // Start is called before the first frame update
    void Start()
    {
        LoginButton.onClick.AddListener(LogIn);
        LogoutButton.onClick.AddListener(LogOut);
    }

    // Update is called once per frame
    void Update()
    {

    }

    void LogIn()
    {
        PluginClass.Call("TryLogin", new AndroidPluginCallback());
    }

    void LogOut()
    {
        PluginClass.Call("TryLogout", new AndroidPluginCallback());
    }
}
