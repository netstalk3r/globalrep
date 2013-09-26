package com.tideworks.mainsail.adapter.gate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.tideworks.core.domain.db.equipment.DbEquipment;
import com.tideworks.mainsail.codes.EquipmentType;

public class MarryDivorceAdapterTest
{
    String ctrNbr = "CTR_NBR";
    String chsNbr = "CHS_NBR";
    String accNbr = "ACC_NBR";

    // *** START divorceEquipment ***

    @Test(expected = NullPointerException.class)
    public void divorceEquipment_husbandIsNull_throwException()
    {
        // given
        DbEquipment spouseCtrChs = null;
        DbEquipment wifeChs = new DbEquipment();

        // do
    }

    @Test(expected = NullPointerException.class)
    public void divorceEquipment_WifeIsNull_throwException()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = null;

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void divorceEquipment_husbandHasInvalidEqType_throwException()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtrChs.setSztpClass("Invalid");
        wifeChs.setSztpClass(EquipmentType.CTR.name());

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void divorceEquipment_wifeHasInvalidEqType_throwException()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtrChs.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setSztpClass("Invalid");

        // do
    }

    @Test
    public void divorceEquipment_divorceCtrAndChs_divorcedSuccessfully()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtrChs.setNbr(ctrNbr);
        spouseCtrChs.setSztpClass(EquipmentType.CTR.name());
        spouseCtrChs.setChsNbr(chsNbr);
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());
        wifeChs.setCtrNbr(ctrNbr);

        // do

        // verify
        assertNull(spouseCtrChs.getChsNbr());
        assertNull(wifeChs.getCtrNbr());

    }

    @Test
    public void divorceEquipment_divorceCtrAndAcc_divorcedSuccessfully()
    {
        // given
        DbEquipment spouseCtrAcc = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        spouseCtrAcc.setNbr(ctrNbr);
        spouseCtrAcc.setSztpClass(EquipmentType.CTR.name());
        spouseCtrAcc.setAccNbr(accNbr);
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());
        wifeAcc.setCtrNbr(ctrNbr);

        // do

        // verify
        assertNull(spouseCtrAcc.getAccNbr());
        assertNull(wifeAcc.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceChsAndAcc_divorcedSuccessfully()
    {
        // given
        DbEquipment spouseChsAcc = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        spouseChsAcc.setNbr(chsNbr);
        spouseChsAcc.setSztpClass(EquipmentType.CHS.name());
        spouseChsAcc.setAccNbr(accNbr);
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());
        wifeAcc.setChsNbr(chsNbr);

        // do

        // verify
        assertNull(spouseChsAcc.getAccNbr());
        assertNull(wifeAcc.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceCtrAndCtr_divorcedSuccessfully()
    {
        // given
        DbEquipment spouseCtrCtr = new DbEquipment();
        DbEquipment wifeCtr = new DbEquipment();
        spouseCtrCtr.setNbr(ctrNbr);
        spouseCtrCtr.setSztpClass(EquipmentType.CTR.name());
        spouseCtrCtr.setAccNbr(ctrNbr);
        wifeCtr.setNbr(ctrNbr);
        wifeCtr.setSztpClass(EquipmentType.CTR.name());
        wifeCtr.setChsNbr(ctrNbr);

        // do

        // verify
        assertNull(spouseCtrCtr.getCtrNbr());
        assertNull(wifeCtr.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceEquipmentsThatAlreadyDivorced_divorcedSuccessfully()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtrChs.setNbr(ctrNbr);
        spouseCtrChs.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());

        // do

        // verify
        assertNull(spouseCtrChs.getCtrNbr());
        assertNull(wifeChs.getCtrNbr());
    }

    // *** END divorceEquipment ***

    // *** START marryEquipment ***
    @Test(expected = NullPointerException.class)
    public void marryEquipment_husbandIsNull_throwException()
    {
        // given
        DbEquipment spouseCtrChs = null;
        DbEquipment wifeChs = new DbEquipment();

        // do
    }

    @Test(expected = NullPointerException.class)
    public void marryEquipment_WifeIsNull_throwException()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = null;

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void marryEquipment_husbandHasInvalidEqType_throwException()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtrChs.setSztpClass("Invalid");
        wifeChs.setSztpClass(EquipmentType.CTR.name());

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void marryEquipment_wifeHasInvalidEqType_throwException()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtrChs.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setSztpClass("Invalid");

        // do
    }

    @Test
    public void marryEquipment_marryCtrAndChs_marrySuccessfully()
    {
        // given
        DbEquipment spouseCtrChs = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtrChs.setNbr(ctrNbr);
        spouseCtrChs.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());

        // do

        // verify
        assertEquals(chsNbr,spouseCtrChs.getChsNbr());
        assertEquals(ctrNbr,wifeChs.getCtrNbr());
    }

    @Test
    public void marryEquipment_marryCtrAndAcc_marrySuccessfully()
    {
        // given
        DbEquipment spouseCtrAcc = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        spouseCtrAcc.setNbr(ctrNbr);
        spouseCtrAcc.setSztpClass(EquipmentType.CTR.name());
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());

        // do

        // verify
        assertEquals(accNbr,spouseCtrAcc.getAccNbr());
        assertEquals(ctrNbr,wifeAcc.getCtrNbr());
    }

    @Test
    public void marryEquipment_marryChsAndAcc_marrySuccessfully()
    {
        // given
        DbEquipment spouseChsAcc = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        spouseChsAcc.setNbr(chsNbr);
        spouseChsAcc.setSztpClass(EquipmentType.CHS.name());
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());

        // do

        // verify
        assertEquals(accNbr,spouseChsAcc.getAccNbr());
        assertEquals(chsNbr,wifeAcc.getChsNbr());
    }

    @Test
    public void marryEquipment_marryCtrAndCtr_marrySuccessfully()
    {
        // given
        DbEquipment spouseCtrCtr = new DbEquipment();
        DbEquipment wifeCtr = new DbEquipment();
        spouseCtrCtr.setNbr(ctrNbr);
        spouseCtrCtr.setSztpClass(EquipmentType.CTR.name());
        wifeCtr.setNbr(ctrNbr);
        wifeCtr.setSztpClass(EquipmentType.CTR.name());

        // do

        // verify
        assertEquals(accNbr,spouseCtrCtr.getCtrNbr());
        assertEquals(chsNbr,wifeCtr.getCtrNbr());
    }

    @Test
    public void marryEquipment_marryEquipmentsThatAlreadyMarried_marrySuccessfully()
    {
        // given
        DbEquipment spouseCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        spouseCtr.setNbr(ctrNbr);
        spouseCtr.setSztpClass(EquipmentType.CTR.name());
        spouseCtr.setChsNbr(chsNbr);
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());
        wifeChs.setCtrNbr(ctrNbr);

        // do

        // verify
        assertEquals(accNbr,spouseCtr.getCtrNbr());
        assertEquals(chsNbr,wifeChs.getCtrNbr());
    }

    // *** END marryEquipment ***
}
