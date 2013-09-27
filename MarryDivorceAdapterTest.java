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
        DbEquipment husbandCtr = null;
        DbEquipment wifeChs = new DbEquipment();

        // do
    }

    @Test(expected = NullPointerException.class)
    public void divorceEquipment_wifeIsNull_throwException()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = null;

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void divorceEquipment_husbandHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setSztpClass("Invalid");
        wifeChs.setSztpClass(EquipmentType.CTR.name());

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void divorceEquipment_wifeHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setSztpClass("Invalid");

        // do
    }

    @Test
    public void divorceEquipment_divorceCtrAndChs_divorcedSuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        husbandCtr.setChsNbr(chsNbr);
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());
        wifeChs.setCtrNbr(ctrNbr);

        // do

        // verify
        assertNull(husbandCtr.getChsNbr());
        assertNull(wifeChs.getCtrNbr());

    }

    @Test
    public void divorceEquipment_divorceCtrAndAcc_divorcedSuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        husbandCtr.setAccNbr(accNbr);
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());
        wifeAcc.setCtrNbr(ctrNbr);

        // do

        // verify
        assertNull(husbandCtr.getAccNbr());
        assertNull(wifeAcc.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceChsAndAcc_divorcedSuccessfully()
    {
        // given
        DbEquipment husbandChs = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        husbandChs.setNbr(chsNbr);
        husbandChs.setSztpClass(EquipmentType.CHS.name());
        husbandChs.setAccNbr(accNbr);
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());
        wifeAcc.setChsNbr(chsNbr);

        // do

        // verify
        assertNull(husbandChs.getAccNbr());
        assertNull(wifeAcc.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceSameTypeEquipments_divorcedSuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeCtr = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        husbandCtr.setAccNbr(ctrNbr);
        wifeCtr.setNbr(ctrNbr);
        wifeCtr.setSztpClass(EquipmentType.CTR.name());
        wifeCtr.setChsNbr(ctrNbr);

        // do

        // verify
        assertNull(husbandCtr.getCtrNbr());
        assertNull(wifeCtr.getCtrNbr());
    }

    @Test
    public void divorceEquipment_divorceNotMarriedEquipments_divorcedSuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());

        // do

        // verify
        assertNull(husbandCtr.getCtrNbr());
        assertNull(wifeChs.getCtrNbr());
    }

    // *** END divorceEquipment ***

    // *** START marryEquipment ***
    @Test(expected = NullPointerException.class)
    public void marryEquipment_husbandIsNull_throwException()
    {
        // given
        DbEquipment husbandCtr = null;
        DbEquipment wifeChs = new DbEquipment();

        // do
    }

    @Test(expected = NullPointerException.class)
    public void marryEquipment_WifeIsNull_throwException()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = null;

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void marryEquipment_husbandHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setSztpClass("Invalid");
        wifeChs.setSztpClass(EquipmentType.CTR.name());

        // do
    }

    @Test(expected = IllegalArgumentException.class)
    public void marryEquipment_wifeHasInvalidEqType_throwException()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setSztpClass("Invalid");

        // do
    }

    @Test
    public void marryEquipment_marryCtrAndChs_marrySuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());

        // do

        // verify
        assertEquals(chsNbr,husbandCtr.getChsNbr());
        assertEquals(ctrNbr,wifeChs.getCtrNbr());
    }

    @Test
    public void marryEquipment_marryCtrAndAcc_marrySuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());

        // do

        // verify
        assertEquals(accNbr,husbandCtr.getAccNbr());
        assertEquals(ctrNbr,wifeAcc.getCtrNbr());
    }

    @Test
    public void marryEquipment_marryChsAndAcc_marrySuccessfully()
    {
        // given
        DbEquipment husbandChs = new DbEquipment();
        DbEquipment wifeAcc = new DbEquipment();
        husbandChs.setNbr(chsNbr);
        husbandChs.setSztpClass(EquipmentType.CHS.name());
        wifeAcc.setNbr(accNbr);
        wifeAcc.setSztpClass(EquipmentType.ACC.name());

        // do

        // verify
        assertEquals(accNbr,husbandChs.getAccNbr());
        assertEquals(chsNbr,wifeAcc.getChsNbr());
    }

    @Test
    public void marryEquipment_marrySameTypeEquipments_marrySuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeCtr = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        wifeCtr.setNbr(ctrNbr);
        wifeCtr.setSztpClass(EquipmentType.CTR.name());

        // do

        // verify
        assertEquals(accNbr,husbandCtr.getCtrNbr());
        assertEquals(chsNbr,wifeCtr.getCtrNbr());
    }

    @Test
    public void marryEquipment_marryMarriedEquipments_marrySuccessfully()
    {
        // given
        DbEquipment husbandCtr = new DbEquipment();
        DbEquipment wifeChs = new DbEquipment();
        husbandCtr.setNbr(ctrNbr);
        husbandCtr.setSztpClass(EquipmentType.CTR.name());
        husbandCtr.setChsNbr(chsNbr);
        wifeChs.setNbr(chsNbr);
        wifeChs.setSztpClass(EquipmentType.CHS.name());
        wifeChs.setCtrNbr(ctrNbr);

        // do

        // verify
        assertEquals(accNbr,husbandCtr.getCtrNbr());
        assertEquals(chsNbr,wifeChs.getCtrNbr());
    }

    // *** END marryEquipment ***
}
