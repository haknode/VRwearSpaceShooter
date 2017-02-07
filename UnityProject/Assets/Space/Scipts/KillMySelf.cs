using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class KillMySelf : MonoBehaviour
{

    public float timeLimit = 0;
    private float timeElapsed;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update ()
	{
        timeElapsed += Time.deltaTime;
        if(timeElapsed > timeLimit)
            Destroy(gameObject);
	}
}
