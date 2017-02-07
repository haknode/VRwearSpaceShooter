using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using UnityEngine;

public class GameManager : MonoBehaviour
{
    private GameManager() : base()
    {
        GameStarted = false;
    }

    public static GameManager instance;
    public UI_Manager ui;
    public Player player;

    public bool GameStarted { get; private set; }
    public int Score { get; private set; }
    public int OldScore { get; private set; }

    void Awake()
    {
        if (instance == null)
        {
            instance = this;
        }
        else if (instance != this)
        {
            Destroy(gameObject);
        }

        DontDestroyOnLoad(gameObject);
    }

    public void StartGame()
    {
        Score = 0;
        GameStarted = true;
        ui.StartPlay();
        player.StartPlay();
    }

    public void EndGame()
    {
        GameStarted = false;
        player._rigidbody.transform.position = Vector3.zero;
        ui.EndPlay();
        if (Score > OldScore)
        {
            OldScore = Score;
            Save();
        }
        
    }

    // Use this for initialization
	void Start ()
	{
	    Load();
	}
	
	// Update is called once per frame
	void Update () {
		
	}


    public void addScore(int scoretoadd)
    {
        Score += scoretoadd;
    }

    public void Save()
    {
        BinaryFormatter bf = new BinaryFormatter();
        FileStream file = File.Create(Application.persistentDataPath + "/vrwearDATA.dat");

        PlayerData data = new PlayerData();
        data.Score = Score;

        bf.Serialize(file, data);
        file.Close();
    }

    public void Load()
    {
        if (File.Exists(Application.persistentDataPath + "/vrwearDATA.dat"))
        {
            BinaryFormatter bf = new BinaryFormatter();
            FileStream file = File.Open(Application.persistentDataPath + "/vrwearDATA.dat", FileMode.Open);
            PlayerData data = (PlayerData)bf.Deserialize(file);
            file.Close();

            OldScore = data.Score;
        }
    }

    [Serializable]
    class PlayerData
    {
        public int Score { get; set; }
    }
}
