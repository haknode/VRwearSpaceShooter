using UnityEngine;
using System.Collections;

public class OpponentSpawner : MonoBehaviour
{
    public GameObject OpponentClass;
    private float _spawnTimeout = 3f;
    private float _lastSpawn = 0f;
    public GameObject PlayerInstance;
	
	// Update is called once per frame
	void Update () {
        if (GameManager.instance.GameStarted && ((Time.time - _lastSpawn) > _spawnTimeout))
        {
            _lastSpawn = Time.time;
            Vector3 location = new Vector3(PlayerInstance.transform.position.x + 5, PlayerInstance.transform.position.y + Random.Range(-8,8), PlayerInstance.transform.position.z);
            location = Quaternion.Euler(0, Random.Range(0,360), 0) * location;
            GameObject go = GameObject.Instantiate(OpponentClass, transform.localPosition + location, Quaternion.identity) as GameObject;
            Opponent o = go.GetComponent<Opponent>();
            o.Player = PlayerInstance;
            Debug.Log("Spawn Enemy");
        }
    }
}
