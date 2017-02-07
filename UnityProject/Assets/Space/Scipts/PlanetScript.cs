using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlanetScript : MonoBehaviour
{

    void OnTriggerEnter(Collider collider)
    {
        Player player = collider.GetComponent<Player>();
      
        if (player != null)
        {
            Debug.Log("Player dies because crash into Planet");
            player.Die(true);
        }
    }

}
