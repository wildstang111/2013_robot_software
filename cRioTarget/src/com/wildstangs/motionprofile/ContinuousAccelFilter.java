/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * This is a port to java of a c++ Filter used by Team 254 in 2011 under the BSD -2 license
 * https://github.com/Team254/FRC-2011
 */

/*
Copyright (c) 2011, Team 254 
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project. */

package com.wildstangs.motionprofile;


public class ContinuousAccelFilter {
    protected double currPos; 
    protected double currVel; 
    protected double currAcc; 

    public double getCurrPos() {
        return currPos;
    }

    public double getCurrVel() {
        return currVel;
    }

    public double getCurrAcc() {
        return currAcc;
    }
    
    
    public ContinuousAccelFilter(double currPos , double currVel , double currAcc) {
        this.currPos = currPos; 
        this.currVel = currVel; 
        this.currAcc = currAcc; 
    }
    
    public void updateValues(double acc, double dt) {
        currPos+=currVel*dt+acc*.5*dt*dt;
        currVel+=acc*dt;
        currAcc=acc;
    }

    private double dt2,a,const_time,dtf,af;
    public void calculateSystem(double distance_to_target, double v, double goal_v, double max_a, double max_v, double dt)
    {
        dt2 = 0 ; 
        a = 0;
        const_time = 0 ;
        dtf= 0 ;
        af= 0 ;
        maxAccelTime(distance_to_target,v,goal_v,max_a,max_v);
        double time_left=dt;
        if(dt2>time_left)
            updateValues(a,time_left);
        else {
            updateValues(a,dt2);
            time_left-=dt2;
            if(const_time>time_left)
                updateValues(0,time_left);
            else {
                updateValues(0,const_time);
                time_left-=const_time;
                if(dtf>time_left)
                    updateValues(af,time_left);
                else {
                    updateValues(af,dtf);
                    time_left-=dtf;
                    updateValues(0,time_left);
                }
            }
        }
    }
    
    public void maxAccelTime(double distance_left, double curr_vel, double goal_vel, double max_a, double max_v)
    {
        double local_const_time=0;
        double start_a=0;
        if(distance_left > 0)
            start_a=max_a;
        else if(distance_left==0) {
            dt2=0;
            a=0;
            const_time=0;
            dtf=0;
            af=0;
            return;
        }
        else {
            maxAccelTime(-distance_left, -curr_vel, -goal_vel, max_a, max_v);
            a*=-1;
            af*=-1;
            return;
        }
        double max_accel_velocity = distance_left * 2 * Math.abs(start_a) + curr_vel * curr_vel;
        if(max_accel_velocity > 0)
            max_accel_velocity=Math.sqrt(max_accel_velocity);
        else
            max_accel_velocity=-Math.sqrt(-max_accel_velocity);

        double final_a;
        if(max_accel_velocity>goal_vel)
            final_a=-max_a;
        else
            final_a=max_a;

        double top_v = Math.sqrt((distance_left + (curr_vel * curr_vel) / (2.0 * start_a) + (goal_vel * goal_vel) / (2.0 * final_a)) / (-1.0 / (2.0 * final_a) + 1.0 / (2.0 * start_a)));

        double accel_time=0;
        if(top_v > max_v) {
            accel_time = (max_v - curr_vel) / max_a;
            local_const_time = (distance_left + (goal_vel * goal_vel - max_v * max_v) / (2.0 * max_a)) / max_v;
        }
        else
            accel_time = (top_v - curr_vel) / start_a;

        dt2=accel_time;
        a=start_a;
        const_time=local_const_time;
        dtf=(goal_vel-top_v)/final_a;
        af=final_a;
    }
    
}
