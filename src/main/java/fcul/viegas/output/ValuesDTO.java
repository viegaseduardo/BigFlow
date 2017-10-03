/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.output;

/**
 *
 * @author viegas
 */
public class ValuesDTO {

    public int nNormal = 0;
    public int nSusp = 0;
    public int nAnomalous = 0;
    public int accNormal = 0;
    public int accSusp = 0;
    public int accAnomalous = 0;
    public int accATK;

    public float floatAccAcceptNormal = 0.0f;
    public float floatAccAcceptAttack = 0.0f;
    public float floatAccAccept = 0.0f;
    public float floatRejection = 0.0f;
    public float floatRejectionAttack = 0.0f;
    public float floatRejectionNormal = 0.0f;
    public float floatAVGAvgAccuracy = 0.0f;
    public float floatClassificationQuality = 0.0f;
    public float floatCorrectlyRejected = 0.0f;
    public float floatCorrectlyRejectedNormal = 0.0f;
    public float floatCorrectlyRejectedAttack = 0.0f;

    public float nMeasures;
}
