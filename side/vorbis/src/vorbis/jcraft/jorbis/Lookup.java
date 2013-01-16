/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/* JOrbis
 * Copyright (C) 2000 ymnk, JCraft,Inc.
 *
 * Written by: 2000 ymnk<ymnk@jcraft.com>
 *
 * Many thanks to
 *   Monty <monty@xiph.org> and
 *   The XIPHOPHORUS Company http://www.xiph.org/ .
 * JOrbis has been based on their awesome works, Vorbis codec.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package vorbis.jcraft.jorbis;

class Lookup{
  static final int COS_LOOKUP_SZ=128;
  static final float[] COS_LOOKUP= {+1.0000000000000f, +0.9996988186962f,
      +0.9987954562052f, +0.9972904566787f, +0.9951847266722f,
      +0.9924795345987f, +0.9891765099648f, +0.9852776423889f,
      +0.9807852804032f, +0.9757021300385f, +0.9700312531945f,
      +0.9637760657954f, +0.9569403357322f, +0.9495281805930f,
      +0.9415440651830f, +0.9329927988347f, +0.9238795325113f,
      +0.9142097557035f, +0.9039892931234f, +0.8932243011955f,
      +0.8819212643484f, +0.8700869911087f, +0.8577286100003f,
      +0.8448535652497f, +0.8314696123025f, +0.8175848131516f,
      +0.8032075314806f, +0.7883464276266f, +0.7730104533627f,
      +0.7572088465065f, +0.7409511253550f, +0.7242470829515f,
      +0.7071067811865f, +0.6895405447371f, +0.6715589548470f,
      +0.6531728429538f, +0.6343932841636f, +0.6152315905806f,
      +0.5956993044924f, +0.5758081914178f, +0.5555702330196f,
      +0.5349976198871f, +0.5141027441932f, +0.4928981922298f,
      +0.4713967368260f, +0.4496113296546f, +0.4275550934303f,
      +0.4052413140050f, +0.3826834323651f, +0.3598950365350f,
      +0.3368898533922f, +0.3136817403989f, +0.2902846772545f,
      +0.2667127574749f, +0.2429801799033f, +0.2191012401569f,
      +0.1950903220161f, +0.1709618887603f, +0.1467304744554f,
      +0.1224106751992f, +0.0980171403296f, +0.0735645635997f,
      +0.0490676743274f, +0.0245412285229f, +0.0000000000000f,
      -0.0245412285229f, -0.0490676743274f, -0.0735645635997f,
      -0.0980171403296f, -0.1224106751992f, -0.1467304744554f,
      -0.1709618887603f, -0.1950903220161f, -0.2191012401569f,
      -0.2429801799033f, -0.2667127574749f, -0.2902846772545f,
      -0.3136817403989f, -0.3368898533922f, -0.3598950365350f,
      -0.3826834323651f, -0.4052413140050f, -0.4275550934303f,
      -0.4496113296546f, -0.4713967368260f, -0.4928981922298f,
      -0.5141027441932f, -0.5349976198871f, -0.5555702330196f,
      -0.5758081914178f, -0.5956993044924f, -0.6152315905806f,
      -0.6343932841636f, -0.6531728429538f, -0.6715589548470f,
      -0.6895405447371f, -0.7071067811865f, -0.7242470829515f,
      -0.7409511253550f, -0.7572088465065f, -0.7730104533627f,
      -0.7883464276266f, -0.8032075314806f, -0.8175848131516f,
      -0.8314696123025f, -0.8448535652497f, -0.8577286100003f,
      -0.8700869911087f, -0.8819212643484f, -0.8932243011955f,
      -0.9039892931234f, -0.9142097557035f, -0.9238795325113f,
      -0.9329927988347f, -0.9415440651830f, -0.9495281805930f,
      -0.9569403357322f, -0.9637760657954f, -0.9700312531945f,
      -0.9757021300385f, -0.9807852804032f, -0.9852776423889f,
      -0.9891765099648f, -0.9924795345987f, -0.9951847266722f,
      -0.9972904566787f, -0.9987954562052f, -0.9996988186962f,
      -1.0000000000000f,};

  /* interpolated lookup based cos function, domain 0 to PI only */
  static float coslook(float a){
    double d=a*(.31830989*(float)COS_LOOKUP_SZ);
    int i=(int)d;
    return COS_LOOKUP[i]+((float)(d-i))*(COS_LOOKUP[i+1]-COS_LOOKUP[i]);
  }

  static final int INVSQ_LOOKUP_SZ=32;
  static final float[] INVSQ_LOOKUP= {1.414213562373f, 1.392621247646f,
      1.371988681140f, 1.352246807566f, 1.333333333333f, 1.315191898443f,
      1.297771369046f, 1.281025230441f, 1.264911064067f, 1.249390095109f,
      1.234426799697f, 1.219988562661f, 1.206045378311f, 1.192569588000f,
      1.179535649239f, 1.166919931983f, 1.154700538379f, 1.142857142857f,
      1.131370849898f, 1.120224067222f, 1.109400392450f, 1.098884511590f,
      1.088662107904f, 1.078719779941f, 1.069044967650f, 1.059625885652f,
      1.050451462878f, 1.041511287847f, 1.032795558989f, 1.024295039463f,
      1.016001016002f, 1.007905261358f, 1.000000000000f,};

  /* interpolated 1./sqrt(p) where .5 <= p < 1. */
  static float invsqlook(float a){
    double d=a*(2.f*(float)INVSQ_LOOKUP_SZ)-(float)INVSQ_LOOKUP_SZ;
    int i=(int)d;
    return INVSQ_LOOKUP[i]+((float)(d-i))*(INVSQ_LOOKUP[i+1]-INVSQ_LOOKUP[i]);
  }

  static final int INVSQ2EXP_LOOKUP_MIN=-32;
  static final int INVSQ2EXP_LOOKUP_MAX=32;
  static final float[] INVSQ2EXP_LOOKUP= {65536.f, 46340.95001f, 32768.f,
      23170.47501f, 16384.f, 11585.2375f, 8192.f, 5792.618751f, 4096.f,
      2896.309376f, 2048.f, 1448.154688f, 1024.f, 724.0773439f, 512.f,
      362.038672f, 256.f, 181.019336f, 128.f, 90.50966799f, 64.f, 45.254834f,
      32.f, 22.627417f, 16.f, 11.3137085f, 8.f, 5.656854249f, 4.f,
      2.828427125f, 2.f, 1.414213562f, 1.f, 0.7071067812f, 0.5f, 0.3535533906f,
      0.25f, 0.1767766953f, 0.125f, 0.08838834765f, 0.0625f, 0.04419417382f,
      0.03125f, 0.02209708691f, 0.015625f, 0.01104854346f, 0.0078125f,
      0.005524271728f, 0.00390625f, 0.002762135864f, 0.001953125f,
      0.001381067932f, 0.0009765625f, 0.000690533966f, 0.00048828125f,
      0.000345266983f, 0.000244140625f, 0.0001726334915f, 0.0001220703125f,
      8.631674575e-05f, 6.103515625e-05f, 4.315837288e-05f, 3.051757812e-05f,
      2.157918644e-05f, 1.525878906e-05f,};

  /* interpolated 1./sqrt(p) where .5 <= p < 1. */
  static float invsq2explook(int a){
    return INVSQ2EXP_LOOKUP[a-INVSQ2EXP_LOOKUP_MIN];
  }

  static final int FROMdB_LOOKUP_SZ=35;
  static final int FROMdB2_LOOKUP_SZ=32;
  static final int FROMdB_SHIFT=5;
  static final int FROMdB2_SHIFT=3;
  static final int FROMdB2_MASK=31;
  static final float[] FROMdB_LOOKUP= {1.f, 0.6309573445f, 0.3981071706f,
      0.2511886432f, 0.1584893192f, 0.1f, 0.06309573445f, 0.03981071706f,
      0.02511886432f, 0.01584893192f, 0.01f, 0.006309573445f, 0.003981071706f,
      0.002511886432f, 0.001584893192f, 0.001f, 0.0006309573445f,
      0.0003981071706f, 0.0002511886432f, 0.0001584893192f, 0.0001f,
      6.309573445e-05f, 3.981071706e-05f, 2.511886432e-05f, 1.584893192e-05f,
      1e-05f, 6.309573445e-06f, 3.981071706e-06f, 2.511886432e-06f,
      1.584893192e-06f, 1e-06f, 6.309573445e-07f, 3.981071706e-07f,
      2.511886432e-07f, 1.584893192e-07f,};
  static final float[] FROMdB2_LOOKUP= {0.9928302478f, 0.9786445908f,
      0.9646616199f, 0.9508784391f, 0.9372921937f, 0.92390007f, 0.9106992942f,
      0.8976871324f, 0.8848608897f, 0.8722179097f, 0.8597555737f,
      0.8474713009f, 0.835362547f, 0.8234268041f, 0.8116616003f, 0.8000644989f,
      0.7886330981f, 0.7773650302f, 0.7662579617f, 0.755309592f, 0.7445176537f,
      0.7338799116f, 0.7233941627f, 0.7130582353f, 0.7028699885f,
      0.6928273125f, 0.6829281272f, 0.6731703824f, 0.6635520573f,
      0.6540711597f, 0.6447257262f, 0.6355138211f,};

  /* interpolated lookup based fromdB function, domain -140dB to 0dB only */
  static float fromdBlook(float a){
    int i=(int)(a*((float)(-(1<<FROMdB2_SHIFT))));
    return (i<0) ? 1.f : ((i>=(FROMdB_LOOKUP_SZ<<FROMdB_SHIFT)) ? 0.f
        : FROMdB_LOOKUP[i>>>FROMdB_SHIFT]*FROMdB2_LOOKUP[i&FROMdB2_MASK]);
  }

}
