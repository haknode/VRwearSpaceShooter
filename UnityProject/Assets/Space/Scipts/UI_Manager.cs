using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class UI_Manager : MonoBehaviour
{

    public GameObject StartGamePanel;
    public GameObject PlayerUI;
    public Text BestScoreText;
    public Text LifeText;
    public Text PointsText;

	// Use this for initialization
	void Start () {
        PlayerUI.SetActive(true);
        PointsText.gameObject.SetActive(false);
    }
	
	// Update is called once per frame
	void Update () {
	    if (GameManager.instance.GameStarted)
	    {
	        LifeText.text = GameManager.instance.player.Life + " Lifes left!";
            PointsText.text = GameManager.instance.Score + " Points!";
        }
	    else
	    {
	        if(GameManager.instance.player.WatchIsConnected)
                LifeText.text = "Watch connected!";
            else
                LifeText.text = "Watch NOT connected!";
	        BestScoreText.text = "Best Score\n"+GameManager.instance.OldScore;
	    }
    }

    public void StartPlay()
    {
        StartGamePanel.SetActive(false);
        //PlayerUI.SetActive(true);
        PointsText.gameObject.SetActive(true);
        gameObject.transform.parent = GameManager.instance.player._camera.transform;
        gameObject.transform.localPosition = new Vector3(0.2f,-0.2f,1);
        gameObject.transform.localRotation = Quaternion.Euler(0, 0, 0);
        //gameObject.transform.position = GameManager.instance.player._camera.transform.position + GameManager.instance.player._camera.transform.forward * 0.5f;
        //gameObject.transform.rotation = GameManager.instance.player._camera.transform.rotation;
    }

    public void EndPlay()
    {
        PointsText.gameObject.SetActive(false);
        StartGamePanel.SetActive(true);
        //PlayerUI.SetActive(false);
        gameObject.transform.parent = null;
    }
}
