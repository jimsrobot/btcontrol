Type=Activity
Version=2.71
@EndOfDesignText@

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
    'These global variables will be redeclared each time the activity is created.
    'These variables can only be accessed from this module.
    Dim scvMain As ScrollView
    Dim PanelNb As Int            
		PanelNb=20
    Dim PanelHeight As Int    
		PanelHeight=50dip
End Sub

Sub Activity_Create(FirstTime As Boolean)
    Dim i As Int
    
    scvMain.Initialize(500)
    Activity.AddView(scvMain, 0, 0, 100%x, 100%y)
'    Activity.LoadLayout("ScrollViewNLayouts")

    For i=0 To PanelNb-1
        Dim pnltest As Panel
        Dim lblTest As Label
        Dim btnTest As Button
        Dim edtTest As EditText
        
        pnltest.Initialize("pnlTest")
'        pnltest.LoadLayout("Layout")
        scvMain.Panel.AddView(pnltest,0,5dip+i*PanelHeight,100%x,PanelHeight)
        pnltest.Tag=i
        If (i Mod 2)=1 Then
            pnltest.Color=Colors.Red
        Else
            pnltest.Color=Colors.Blue
        End If
        
        lblTest.Initialize("lblTest")
        pnltest.AddView(lblTest,10dip, 5dip, 80dip, 40dip)
        lblTest.Tag=i
        lblTest.TextSize=20
        lblTest.Text="Test "&i
        
        edtTest.Initialize("edtTest")
        pnltest.AddView(edtTest,90dip, 5dip, 110dip, 40dip)
        edtTest.Tag=i
        edtTest.ForceDoneButton=True
        
        btnTest.Initialize("btnTest")
        pnltest.AddView(btnTest,240dip, 5dip, 70dip, 40dip)
        btnTest.Tag=i
        btnTest.Text="Test "&i
    Next
    scvMain.Panel.Height=PanelNb*PanelHeight
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub lblTest_Click
    Dim Send As Label
    
    Send=Sender
    Activity.Title="Label "&Send.Tag
End Sub

Sub btnTest_Click
    Dim Send As Button
    
    Send=Sender
    Activity.Title="Button "&Send.Tag
End Sub

Sub pnlTest_Click
    Dim Send As Panel
    
    Send=Sender
    Activity.Title="Panel "&Send.Tag
End Sub

Sub edtTest_EnterPressed
    Dim Send As EditText
    
    Send=Sender
    Activity.Title="EditText "&Send.Tag&" = "&Send.Text
End Sub