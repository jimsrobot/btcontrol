Type=Activity
Version=2.71
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim btnEditStageOK As Button
	Dim btnEditStageCancel As Button 
	Dim chkBeep As RadioButton
	Dim chkDoNothing As RadioButton
	Dim chkIncreaseTemp As RadioButton
	Dim txtDuration As EditText
	Dim txtRate As EditText
	Dim txtTargetTemp As EditText
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("EditStage")
	'btnEditStageOK.Initialize("btnEditStageOK")
	'btnEditStageCancel.Initialize("btnEditStageCancel")
	
	If StageList.SelectedStageID > -1 Then
		'We need to load the screen
		LoadStage
	Else 
		'This is a new stage
	End If
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub btnEditStageOK_Click
	'Save this stage
	SaveStage
	Activity.Finish
End Sub

Sub btnEditStageCancel_Click
	Activity.Finish 
End Sub



#Region SQL

Sub SaveStage
	Dim sSQL As String
	Dim lMaxOrder As Long 
	If ProfileList.SelectedProfileID >-1 Then
	
		Log(StageList.SelectedStageID)
		
		If StageList.SelectedStageID < 0 Then
			sSQL = "INSERT INTO stages (stage_id, profile_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order) VALUES (Null,"
		Else
			sSQL = "INSERT INTO stages (stage_id, profile_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order) VALUES ("
			sSQL = sSQL & StageList.SelectedStageID & ", "
		End If
		
		sSQL = sSQL & ProfileList.SelectedProfileID & ", "
		
		If chkBeep.Checked Then
			sSQL = sSQL & "3, "
		Else If chkDoNothing.Checked Then
			sSQL = sSQL & "1, "
		Else If chkIncreaseTemp.Checked Then
			sSQL = sSQL & "2, "
		End If
		
		sSQL = sSQL & txtTargetTemp.Text & ", "
		sSQL = sSQL & txtRate.Text & ", "
		sSQL = sSQL & txtDuration.Text & ", "
		
		lMaxOrder = GetMaxOrder 
		lMaxOrder = lMaxOrder + 1
		sSQL= sSQL & lMaxOrder & ")"
		
		Log(sSQL)
		
		Main.SQL1.ExecNonQuery(sSQL)
	Else
		Msgbox("Invalid Profile ID", "ERROR")
	End If
	
End Sub

Sub LoadStage
	Dim Cursor1 As Cursor
	Dim sStageType As String 
		
  Cursor1 = Main.SQL1.ExecQuery("SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE stage_id=" & StageList.SelectedStageID)
  
	If Cursor1.RowCount <> 1 Then
		Msgbox ("Error - too many stages", "ERROR")
		'Exit Sub
	
	Else
	  Cursor1.Position = 0
		Dim lbl As Label
		lbl.Initialize("lbl")
		sStageType = StageTypeToString(Cursor1.Getstring("stage_type"))
		txtDuration.Text = Cursor1.Getstring("stage_seconds")
		txtRate.Text = Cursor1.Getstring("stage_percent")
		txtTargetTemp.Text = Cursor1.Getstring("stage_temp")
		
		Select Cursor1.Getstring("stage_type")
			Case 1
				chkDoNothing.Checked = True
				chkDoNothing_CheckedChange(True)
			Case 2
				chkIncreaseTemp.Checked = True
				chkIncreaseTemp_CheckedChange(True)
			Case 3
				chkBeep.Checked = True
				chkBeep_CheckedChange(True)
			Case Else
				Msgbox ("Error - invalid stage type", "ERROR")
						
		End Select			
	End If					
  Cursor1.Close
	
End Sub

Sub GetMaxOrder() As Long
	Dim ret As Object 
	If ProfileList.SelectedProfileID > -1 Then
		Dim sSQL As String
		sSQL = "SELECT MAX(stage_sort_order) FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID 
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		
		If ret <> Null Then
			Return ret
		Else
			Return 0
		End If	
	Else
		Msgbox("Error getting sort order", "Error")
		Return -1
	End If
End Sub

#Region StageType
Sub StageTypeToString(iStageType As Int) As String 
	Select iStageType
		Case 1
			Return "Do nothing"
		Case 2
			Return "Heat to"
		Case 3
			Return "Beep"
		
	End Select
End Sub

Sub StringToStageType(sStageType As String) As Int 
	Select sStageType
		Case "Do nothing"
			Return 1
		Case "Heat to"
			Return 2
		Case "Beep"
			Return 3
		
	End Select
End Sub
#End Region 

#End Region

Sub chkIncreaseTemp_CheckedChange(Checked As Boolean)
	If Checked = True Then
		'txtTargetTemp.Text = "0"
		txtTargetTemp.Enabled = True
		'txtRate.Text = "0"
		txtRate.Enabled = True
		txtDuration.Text = "0"
		txtDuration.Enabled = False
	End If
End Sub
Sub chkDoNothing_CheckedChange(Checked As Boolean)
	If Checked = True Then
		txtTargetTemp.Text = "0"
		txtTargetTemp.Enabled = False
		txtRate.Text = "0"
		txtRate.Enabled = False
		'txtDuration.Text = "0"
		txtDuration.Enabled = True
	End If
End Sub
Sub chkBeep_CheckedChange(Checked As Boolean)
	If Checked = True Then
		txtTargetTemp.Text = "0"
		txtTargetTemp.Enabled = False
		txtRate.Text = "0"
		txtRate.Enabled = False
		txtDuration.Text = "0"
		txtDuration.Enabled = False
	End If
End Sub