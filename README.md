# CanTest03
CAN emulator for testing ABT / FPK buttons via CANLANC

### Version history:
  - 1.0.1.0: MFL buttons support added
  - 1.0.3.0: ICAS touch messages implemented
  - 1.0.3.1: in ICAS UPDATE message the byte[3] set to 0x11 = PRESS
  - 1.0.4.0: added the DELAY option for ICAS TOUCH messages
  - 1.0.4.1: added an option NOT to send RELEASE messages, X coord. is NOT divided by 2
  - 1.0.4.2: X coord IS divided by 2, Y coord is NOT
