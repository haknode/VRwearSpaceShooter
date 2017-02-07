using UnityEngine;
using System.Collections;

public class Opponent : MonoBehaviour
{
    public GameObject Player;
    private float _shootTimeout = 2f;
    private float _lastTimeShot = 0f;
    public GameObject Projectile;
    public GameObject ExplosionClass;
    private float timeSinceSpawn = 0f;
    private ParticleSystem[] afterburners;
    private Vector3[] afterburnersScales;

    void Start()
    {
        afterburners = GetComponentsInChildren<ParticleSystem>();
        afterburnersScales = new Vector3[afterburners.Length];

        for (int i = 0; i < afterburners.Length; i++)
        {
            afterburnersScales[i] = afterburners[i].transform.localScale;
        }
    }

	// Update is called once per frame
	void Update ()
	{
	    timeSinceSpawn += Time.deltaTime;

	    float scale = Mathf.Clamp(timeSinceSpawn/10f,0,1);
	    transform.localScale = Vector3.one*scale;

	    for (int i = 0; i < afterburners.Length; i++)
	    {
	        afterburners[i].transform.localScale = afterburnersScales[i] * scale;
	    }

	    transform.LookAt(Player.transform.position);

        if (scale >= 1)
	    {
	        if ((Time.time - _lastTimeShot) > _shootTimeout)
	        {
	            _lastTimeShot = Time.time;
	            GameObject.Instantiate(Projectile, transform.localPosition, transform.rotation);
	        }
	    }
	    if (!GameManager.instance.GameStarted)
	    {
	        Die(false);
	    }
    }

    public void Die(bool byPlayer)
    {
        Instantiate(ExplosionClass, transform.position, transform.rotation);

        if (byPlayer)
            GameManager.instance.addScore(1);

        Object.Destroy(gameObject);
    }

    void OnTriggerEnter(Collider collider)
    {
        Player player = collider.GetComponent<Player>();
        
        if (player != null)
        {
            Debug.Log("Player dies because Crash into Opponent");
            player.Die(true);
        }
    }
}
