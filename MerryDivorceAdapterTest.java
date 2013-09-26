package com.tideworks.mainsail.adapter.gate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.tideworks.core.domain.db.equipment.DbEquipment;
import com.tideworks.mainsail.codes.EquipmentType;

public class MerryDivorceAdapterTest
{
    String ctrNbr = "CTR_NBR";
    String chsNbr = "CHS_NBR";
    String accNbr = "ACC_NBR";

    @Test(expected = NullPointerException.class)
    public void divorceEquipment_husbandIsNullWifeIsNotNull_throwException()
    {
        // given
        DbEquipment husband = null;
        DbEquipment wife = new DbEquipment();

        // do
    }

    @Test(expected = NullPointerException.class)
    public void divorceEquipment_husbandIsNotNullWifeIsNull_throwException()
    {
        // given
        DbEquipment husband = new DbEquipment();
        DbEquipment wife = null;

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void divorceEquipment_husbandHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husband = new DbEquipment();
        DbEquipment wife = new DbEquipment();
        husband.setSztpClass("Invalid");
        wife.setSztpClass(EquipmentType.CTR.toString());

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void divorceEquipment_wifeHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husband = new DbEquipment();
        DbEquipment wife = new DbEquipment();
        husband.setSztpClass(EquipmentType.CTR.toString());
        wife.setSztpClass("Invalid");

        // do
    }

    @Test
    public void divorceEquipment_divorceCtrAndChs_divorcedSuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForDivorce(EquipmentType.CTR,chsNbr,EquipmentType.CHS,ctrNbr);
        DbEquipment wife = prepareEqForDivorce(EquipmentType.CHS,chsNbr,EquipmentType.CTR,ctrNbr);

        // do

        // verify
        assertNull(husband.getChsNbr());
        assertNull(wife.getCtrNbr());

    }

    @Test
    public void divorceEquipment_divorceCtrAndAcc_divorcedSuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForDivorce(EquipmentType.CTR,ctrNbr,EquipmentType.ACC,accNbr);
        DbEquipment wife = prepareEqForDivorce(EquipmentType.ACC,accNbr,EquipmentType.CTR,ctrNbr);

        // do

        // verify
        assertNull(husband.getAccNbr());
        assertNull(wife.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceChsAndAcc_divorcedSuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForDivorce(EquipmentType.CHS,chsNbr,EquipmentType.ACC,accNbr);
        DbEquipment wife = prepareEqForDivorce(EquipmentType.ACC,accNbr,EquipmentType.CHS,chsNbr);

        // do

        // verify
        assertNull(husband.getAccNbr());
        assertNull(wife.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceCtrAndCtr_divorcedSuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForDivorce(EquipmentType.CTR,ctrNbr,EquipmentType.CTR,ctrNbr);
        DbEquipment wife = prepareEqForDivorce(EquipmentType.CTR,ctrNbr,EquipmentType.CTR,ctrNbr);

        // do

        // verify
        assertNull(husband.getCtrNbr());
        assertNull(wife.getCtrNbr());
    }

    @Test(expected = NullPointerException.class)
    public void marryEquipment_husbandIsNullWifeIsNotNull_throwException()
    {
        // given
        DbEquipment husband = null;
        DbEquipment wife = new DbEquipment();

        // do
    }

    @Test(expected = NullPointerException.class)
    public void marryEquipment_husbandIsNotNullWifeIsNull_throwException()
    {
        // given
        DbEquipment husband = new DbEquipment();
        DbEquipment wife = null;

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void marryEquipment_husbandHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husband = new DbEquipment();
        DbEquipment wife = new DbEquipment();
        husband.setSztpClass("Invalid");
        wife.setSztpClass(EquipmentType.CTR.toString());

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void marryEquipment_wifeHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husband = new DbEquipment();
        DbEquipment wife = new DbEquipment();
        husband.setSztpClass(EquipmentType.CTR.toString());
        wife.setSztpClass("Invalid");

        // do
    }

    @Test
    public void marryEquipment_merryCtrAndChs_merrySuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForMerry(EquipmentType.CTR,ctrNbr);
        DbEquipment wife = prepareEqForMerry(EquipmentType.CHS,chsNbr);

        // do

        // verify
        assertNotNull(husband.getChsNbr());
        assertEquals(chsNbr,husband.getChsNbr());
        assertNotNull(wife.getCtrNbr());
        assertEquals(ctrNbr,wife.getCtrNbr());
    }

    @Test
    public void merryEquipment_merryCtrAndAcc_merrySuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForMerry(EquipmentType.CTR,ctrNbr);
        DbEquipment wife = prepareEqForMerry(EquipmentType.ACC,accNbr);

        // do

        // verify
        assertNotNull(husband.getAccNbr());
        assertEquals(accNbr,husband.getAccNbr());
        assertNotNull(wife.getCtrNbr());
        assertEquals(ctrNbr,wife.getCtrNbr());
    }

    @Test
    public void merryEquipment_merryChsAndAcc_merrySuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForMerry(EquipmentType.CHS,chsNbr);
        DbEquipment wife = prepareEqForMerry(EquipmentType.ACC,accNbr);

        // do

        // verify
        assertNotNull(husband.getAccNbr());
        assertEquals(accNbr,husband.getAccNbr());
        assertNotNull(wife.getChsNbr());
        assertEquals(chsNbr,wife.getChsNbr());
    }

    public void merryEquipment_merryCtrAndCtr_merrySuccessfully()
    {
        // given
        DbEquipment husband = prepareEqForMerry(EquipmentType.CTR,chsNbr);
        DbEquipment wife = prepareEqForMerry(EquipmentType.CTR,accNbr);

        // do

        // verify
        assertNotNull(husband.getCtrNbr());
        assertEquals(accNbr,husband.getCtrNbr());
        assertNotNull(wife.getCtrNbr());
        assertEquals(chsNbr,wife.getCtrNbr());
    }

    private DbEquipment prepareEqForDivorce(EquipmentType mainEqTp, String mainEqNbr, EquipmentType attachedEqTp,
            String attachedEqNbr)
    {
        DbEquipment eq = new DbEquipment();
        eq.setSztpClass(mainEqNbr.toString());
        eq.setNbr(mainEqNbr);
        switch(attachedEqTp)
        {
            case CTR:
                eq.setCtrNbr(attachedEqNbr);
                break;
            case CHS:
                eq.setChsNbr(attachedEqNbr);
                break;
            case ACC:
                eq.setAccNbr(attachedEqNbr);
                break;
        }
        return eq;
    }

    private DbEquipment prepareEqForMerry(EquipmentType eqTp, String eqNbr)
    {
        DbEquipment eq = new DbEquipment();
        eq.setSztpClass(eqTp.toString());
        eq.setNbr(eqNbr);
        return eq;
    }

}
