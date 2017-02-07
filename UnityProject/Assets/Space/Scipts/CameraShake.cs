using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraShake : MonoBehaviour {

	private bool isShaking = false;
    private float elapsed = 0.0f;
    float duration = 1.5f;
    float magnitude = 0.2f;
    private Camera _camera;

    void Start()
    {
        _camera = GetComponent<Camera>();
    }

    public void StartShake()
    {
        StartCoroutine("Shake");
    }

  
    public void SubtleShake(float strength)
    {
        if (isShaking) return;

        Vector3 originalCamPos = Vector3.zero;

        float x = 0.01f * strength * Random.value * 2.0f;
        float y = 0.01f * strength * Random.value * 2.0f;

        _camera.transform.localPosition = new Vector3(x, y, originalCamPos.z);

    }

    IEnumerator Shake()
    {
        elapsed = 0.0f;
        if (isShaking)
        {
            yield return null;
        }
        Debug.Log("Hello");

        isShaking = true;

        Vector3 originalCamPos = Vector3.zero;

        while (elapsed < duration)
        {

            elapsed += Time.deltaTime;

            float percentComplete = elapsed / duration;
            float damper = 1.0f - Mathf.Clamp(4.0f * percentComplete - 3.0f, 0.0f, 1.0f);

            // map value to [-1, 1]
            float x = Random.value * 2.0f - 1.0f;
            float y = Random.value * 2.0f - 1.0f;
            x *= magnitude * damper;
            y *= magnitude * damper;

            _camera.transform.localPosition = new Vector3(x, y, originalCamPos.z);

            yield return null;
        }

        _camera.transform.localPosition = originalCamPos;
        isShaking = false;
    }
}
