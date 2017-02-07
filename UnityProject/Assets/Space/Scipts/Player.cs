using System;
using UnityEngine;
using System.Collections;
using System.Timers;
using Random = UnityEngine.Random;

public class Player : MonoBehaviour
{

    public Rigidbody _rigidbody;
    public Camera _camera;
    private float _shootTimeout = 0.2f;
    private float _lastTimeShot = 0f;
    public GameObject ProctileGameObjectClass;
    public GvrPointerInputModule pointerRef;

    public int Life { get; private set; }

    private float watchMovementZAxis;
    private float watchMovementhHorizontalAxis;

    private Vector3 _localRotationOffset;

    private CameraShake _cameraShake;

    public bool WatchIsConnected { get; private set; }

    // Use this for initialization
    void Start ()
    {
        pointerRef = GameObject.FindGameObjectsWithTag("Pointer")[0].GetComponent<GvrPointerInputModule>();
        Debug.Log(pointerRef);
        _rigidbody = this.GetComponent<Rigidbody>();
	    _camera = this.GetComponentInChildren<Camera>();
        _cameraShake = _camera.GetComponent<CameraShake>();
	}

    public void StartPlay()
    {
        Life = 10;
    }
	
	// Update is called once per frame
	void Update ()
	{
        if (GameManager.instance.GameStarted) { 
	        float horizontalAxis = Input.GetAxisRaw("Horizontal");
            float zAxis = Input.GetAxisRaw("Vertical");

	        horizontalAxis += watchMovementhHorizontalAxis;
	        zAxis += watchMovementZAxis;

	        horizontalAxis *= 2;
	        zAxis *= 2;

            _cameraShake.SubtleShake(zAxis);

            _rigidbody.AddForce(Quaternion.Euler(_rigidbody.transform.localRotation.eulerAngles * -1f) * _camera.transform.rotation * new Vector3(horizontalAxis, 0f, zAxis));
            _localRotationOffset = Vector3.MoveTowards(_localRotationOffset, new Vector3(0, 0, horizontalAxis * -5f), 1f);
	        _rigidbody.transform.localEulerAngles = _camera.transform.rotation * _localRotationOffset;
        }

        if (Input.GetButtonDown("Fire1"))
	    {
            if(GameManager.instance.GameStarted)
	            Shoot();
            else
                pointerRef.ClickFromWatch();   //perfom click from Google VR lib
        }
	}

    public void Die(bool completely)
    {
        _cameraShake.StartShake();
        _rigidbody.velocity = Vector3.zero;

        if (completely)
            Life = 0;

        if (--Life <= 0)
        {
            GameManager.instance.EndGame();
        }
    }

    /// <summary>
    /// Spawn a new Projectile object.
    /// Only possible if the _shootTimeout has expired!
    /// </summary>
    public void Shoot()
    {
        if (((Time.time - _lastTimeShot) > _shootTimeout))
        {
            _lastTimeShot = Time.time;
            GameObject go = GameObject.Instantiate(ProctileGameObjectClass, transform.localPosition - (_camera.transform.up * .2f), _camera.transform.rotation) as GameObject;
            go.GetComponent<Projectile>().Friendly = true;
        }
    }

    /// <summary>
    /// Method is called from the android library if an orientation changed event is received from the watch.
    /// </summary>
    /// <param name="message">a string with the encoded orientation for each axis</param>
    public void WearOrientationChanged(string message)
    {
        var trashhold = 0.3;

        //parse orientation values for the axis out of the message string
        //Message format: 'x y z'

        var xyzOrientation = message.Split(" ".ToCharArray());

        float xOrientation = float.Parse(xyzOrientation[0]);
        float yOrientation = float.Parse(xyzOrientation[1]);
        float zOrientation = float.Parse(xyzOrientation[2]);

        if (yOrientation < -trashhold)
            watchMovementZAxis = -1;    //movement to the left
        else if (yOrientation > trashhold)
            watchMovementZAxis = 1;     //movement to the right
        else
            watchMovementZAxis = 0;     //no movement

        if (zOrientation < -trashhold)
            watchMovementhHorizontalAxis = -1;  //movement backward
        else if (zOrientation > trashhold)
            watchMovementhHorizontalAxis = 1;   //movement forward
        else
            watchMovementhHorizontalAxis = 0;     //no movement
    }

    /// <summary>
    /// Method is called from the android library if an button clicked event is received from the watch
    /// </summary>
    /// <param name="message">empty</param>
    public void WearButtonClicked(string message)
    {
        //no data in message

        if(GameManager.instance.GameStarted)
            Shoot();
        else
            pointerRef.ClickFromWatch();    //perfom click from Google VR lib
    }

    /// <summary>
    /// Method is called from the android library if the connection to the watch is established or closed.
    /// </summary>
    /// <param name="message">"true" if the watch is connected "false" if not</param>
    public void IsConnected(string message)
    {
        bool isConnected = Boolean.Parse(message);

        WatchIsConnected = isConnected;
    }


    //acceleration (movement) is not used
    //public void WearMovementChanged(string message)
    //{
    //    var xyzMovement = message.Split(" ".ToCharArray());

    //    float xMovement = float.Parse(xyzMovement[0]);
    //    float yMovement = float.Parse(xyzMovement[1]);
    //    float zMovement = float.Parse(xyzMovement[2]);

    //    var newPosition = new Vector3();

    //    newPosition.x = transform.position.x + xMovement;
    //    newPosition.y = transform.position.y + yMovement;
    //    newPosition.z = transform.position.z + zMovement;

    //    transform.position = newPosition;
    //}

}
