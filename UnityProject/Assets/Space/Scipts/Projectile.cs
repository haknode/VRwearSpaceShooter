using UnityEngine;
using System.Collections;

public class Projectile : MonoBehaviour
{

    private Rigidbody _rigidbody;
    private float _speed = 5f;
    public bool Friendly = false;

    // Use this for initialization
    void Start ()
	{
	    _rigidbody = this.GetComponent<Rigidbody>();

        StartCoroutine("Die");
	}
	
	// Update is called once per frame
	void Update ()
	{
	    transform.Translate(0f,0f,_speed*Time.deltaTime,Space.Self);
        GetComponentInChildren<Renderer>().material.color = Friendly ? Color.green : Color.red;
    }

    IEnumerator Die()
    {
        yield return new WaitForSeconds(10f);
        Object.Destroy(gameObject);
        yield return null;
    }

    void OnTriggerEnter(Collider collider)
    {
        Player player = collider.GetComponent<Player>();
        Opponent opponent  = collider.GetComponent<Opponent>();

        if (player != null && !Friendly)
        {
            Debug.Log("Player dies");
            player.Die(false);
        }
        if (opponent != null && Friendly)
        {
            Debug.Log("Opponent dies");
            opponent.Die(true);
        }
    }
}
